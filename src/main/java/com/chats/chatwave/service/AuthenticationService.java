package com.chats.chatwave.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
            throw new EntityNotFoundException("Username already exist. Use a different username ",
                    HttpStatus.BAD_REQUEST);
        }

        Optional<User> emailExist = this.userRepository.findByEmail(requestBody.getEmail());

        if (emailExist.isPresent()) {
            throw new EntityNotFoundException("Email already exist. Use a different email address ",
                    HttpStatus.BAD_REQUEST);
        }

        Optional<User> phonenumberExist = this.userRepository.findByPhonenumber(requestBody.getPhonenumber());

        if (phonenumberExist.isPresent()) {
            throw new EntityNotFoundException("Phonenumber already exist. Use a different phonenumber",
                    HttpStatus.BAD_REQUEST);
        }

        User newUser = User.builder().firstname(requestBody.getFirstname()).lastname(requestBody.getLastname())
                .username(requestBody.getUsername()).email(requestBody.getEmail())
                .enabled(true)
                .phonenumber(requestBody.getPhonenumber()).password(passwordEncoder.encode(requestBody.getPassword()))
                .build();

        User savedUser = this.userRepository.save(newUser);

        String token = jwtService.generateAccessToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        saveToken(token, savedUser);

        var response = AuthenticateResponse.builder().data(buildAuthUserDto(savedUser)).accessToken(token)
                .refreshToken(refreshToken)
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

        String token = jwtService.generateAccessToken(authUser);
        String refreshToken = jwtService.generateRefreshToken(authUser);

        saveToken(token, authUser);

        AuthenticateResponse response = AuthenticateResponse.builder().data(buildAuthUserDto(authUser))
                .accessToken(token)
                .refreshToken(refreshToken)
                .status(HttpStatus.OK).build();

        return response;
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws EntityNotFoundException, StreamWriteException, DatabindException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = null;
        String username = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new EntityNotFoundException("Token not found in authentication berear header",
                    HttpStatus.BAD_REQUEST);
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.substring(7);
            username = jwtService.extractUsername(refreshToken);
        }

        if (username != null) {
            UserDetails userDetails = userRepository.findByUsername(username).orElseThrow();

            if (jwtService.validateToken(refreshToken, userDetails)) {
                String accessToken = jwtService.generateAccessToken((User) userDetails);
                saveToken(accessToken, (User) userDetails);

                AuthenticateResponse authResponse = AuthenticateResponse.builder()
                        .data(buildAuthUserDto((User) userDetails))
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .status(HttpStatus.OK).build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        } else {
            throw new EntityNotFoundException("Invalid Token: User Not Found", HttpStatus.UNAUTHORIZED);
        }

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
