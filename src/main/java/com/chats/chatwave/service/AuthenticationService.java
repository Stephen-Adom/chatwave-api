package com.chats.chatwave.service;

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
import com.chats.chatwave.model.User;
import com.chats.chatwave.model.RequestModel.UserLoginModel;
import com.chats.chatwave.model.RequestModel.UserRequestModel;
import com.chats.chatwave.repository.UserRepository;
import com.chats.chatwave.service.serviceInterface.AuthenticationServiceInterface;

@Service
public class AuthenticationService implements AuthenticationServiceInterface {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @SuppressWarnings("null")
    @Override
    public User userRegistration(UserRequestModel requestBody, BindingResult bindingResult)
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

        return this.userRepository.save(newUser);
    }

    @Override
    public User authenticateUser(UserLoginModel requestBody, BindingResult bindingResult)
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

        return (User) this.userRepository.findByUsername(requestBody.getUsername()).get();
    }
}
