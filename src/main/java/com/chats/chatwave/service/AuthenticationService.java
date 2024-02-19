package com.chats.chatwave.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.chats.chatwave.Exception.EntityNotFoundException;
import com.chats.chatwave.Exception.ValidationErrorsException;
import com.chats.chatwave.model.Token;
import com.chats.chatwave.model.TokenType;
import com.chats.chatwave.model.User;
import com.chats.chatwave.model.Dto.AuthUserDto;
import com.chats.chatwave.model.RequestModel.AuthenticateRequest;
import com.chats.chatwave.model.RequestModel.UserRequestModel;
import com.chats.chatwave.model.ResponseModel.AuthenticateResponse;
import com.chats.chatwave.repository.TokenRepository;
import com.chats.chatwave.repository.UserRepository;
import com.chats.chatwave.service.serviceInterface.AuthenticationServiceInterface;

@Service
public class AuthenticationService implements AuthenticationServiceInterface {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, TokenRepository tokenRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }

    @SuppressWarnings("null")
    @Override
    public AuthenticateResponse userRegistration(UserRequestModel requestBody, BindingResult bindingResult)
            throws ValidationErrorsException, EntityNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorsException(bindingResult.getFieldErrors(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Optional<UserDetails> usernameExist = this.userRepository.findByUsername(requestBody.getUsername());

        if (usernameExist.isPresent()) {
            throw new EntityNotFoundException("Username not found " +
                    usernameExist.get().getUsername(),
                    HttpStatus.NOT_FOUND);
        }

        Optional<User> emailExist = this.userRepository.findByEmail(requestBody.getEmail());

        if (emailExist.isPresent()) {
            throw new EntityNotFoundException("Email not found " +
                    emailExist.get().getEmail(),
                    HttpStatus.NOT_FOUND);
        }

        Optional<User> phonenumberExist = this.userRepository.findByPhonenumber(requestBody.getPhonenumber());

        if (phonenumberExist.isPresent()) {
            throw new EntityNotFoundException("Phonenumber not found " +
                    phonenumberExist.get().getPhonenumber(),
                    HttpStatus.NOT_FOUND);
        }

        User newUser = User.builder().firstname(requestBody.getFirstname()).lastname(requestBody.getLastname())
                .username(requestBody.getUsername()).email(requestBody.getEmail())
                .enabled(true)
                .phonenumber(requestBody.getPhonenumber()).password(passwordEncoder.encode(requestBody.getPassword()))
                .build();

        User savedUser = this.userRepository.save(newUser);

        String token = jwtService.generateToken(savedUser);

        saveToken(token, savedUser);

        var response = AuthenticateResponse.builder().data(buildAuthUserDto(savedUser)).accessToken(token)
                .refreshToken("")
                .status(HttpStatus.CREATED).build();

        return response;
    }

    @Override
    public AuthenticateResponse authenticateUser(AuthenticateRequest requestBody, BindingResult bindingResult)
            throws ValidationErrorsException, EntityNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorsException(bindingResult.getFieldErrors(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(requestBody.getUsername(),
                    requestBody.getPassword());
            this.authenticationManager.authenticate(authentication);

        } catch (Exception e) {
            throw new EntityNotFoundException(e.getMessage() + ": Username or Password not found",
                    HttpStatus.UNAUTHORIZED);
        }

        User authUser = (User) this.userRepository.findByUsername(requestBody.getUsername()).get();

        String token = jwtService.generateToken(authUser);

        saveToken(token, authUser);

        AuthenticateResponse response = AuthenticateResponse.builder().data(buildAuthUserDto(authUser))
                .accessToken(token)
                .refreshToken("")
                .status(HttpStatus.OK).build();

        return response;
    }

    @SuppressWarnings("null")
    private void saveToken(String token, User user) {
        this.revokeUserTokens(user);

        String jwtPayload = token.split("\\.")[1];

        Token tokenObj = Token.builder().user(user).token(jwtPayload).tokenType(TokenType.BEARER).revoked(false)
                .expired(false).build();

        this.tokenRepository.save(tokenObj);
    }

    private void revokeUserTokens(User user) {
        List<Token> userTokens = this.tokenRepository.findAllByUserId(user.getId());
        userTokens.stream().forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        this.tokenRepository.saveAll(userTokens);

    }

    private AuthUserDto buildAuthUserDto(User user) {
        return AuthUserDto.builder().id(user.getId()).firstname(user.getFirstname()).lastname(user.getLastname())
                .username(user.getUsername()).email(user.getEmail()).phonenumber(user.getPhonenumber())
                .bio(user.getBio()).enabled(user.getEnabled()).image(user.getImage()).build();
    }
}
