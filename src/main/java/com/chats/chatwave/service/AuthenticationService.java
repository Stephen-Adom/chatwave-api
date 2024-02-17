package com.chats.chatwave.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.chats.chatwave.Exception.EntityNotFoundException;
import com.chats.chatwave.Exception.ValidationErrorsException;
import com.chats.chatwave.model.User;
import com.chats.chatwave.model.RequestModel.UserRequestModel;
import com.chats.chatwave.repository.UserRepository;
import com.chats.chatwave.service.serviceInterface.AuthenticationServiceInterface;

@Service
public class AuthenticationService implements AuthenticationServiceInterface {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @SuppressWarnings("null")
    @Override
    public User userRegistration(UserRequestModel requestBody, BindingResult bindingResult)
            throws ValidationErrorsException, EntityNotFoundException {
        // if (bindingResult.hasErrors()) {
        // throw new ValidationErrorsException(bindingResult.getFieldErrors(),
        // HttpStatus.UNPROCESSABLE_ENTITY);
        // }

        Optional<UserDetails> usernameExist = this.userRepository.findByUsername(requestBody.getUsername());

        // if (usernameExist.isEmpty()) {
        // throw new EntityNotFoundException("Username not found " +
        // usernameExist.get().getUsername(),
        // HttpStatus.NOT_FOUND);
        // }

        // Optional<User> emailExist =
        // this.userRepository.findByEmail(requestBody.getEmail());

        // if (emailExist.isEmpty()) {
        // throw new EntityNotFoundException("Email not found " +
        // emailExist.get().getEmail(),
        // HttpStatus.NOT_FOUND);
        // }

        // Optional<User> phonenumberExist =
        // this.userRepository.findByPhonenumber(requestBody.getPhonenumber());

        // if (phonenumberExist.isEmpty()) {
        // throw new EntityNotFoundException("Phonenumber not found " +
        // phonenumberExist.get().getPhonenumber(),
        // HttpStatus.NOT_FOUND);
        // }

        User newUser = User.builder().firstname(requestBody.getFirstname()).lastname(requestBody.getLastname())
                .username(requestBody.getUsername()).email(requestBody.getEmail())
                .enabled(true)
                .phonenumber(requestBody.getPhonenumber()).password(passwordEncoder.encode(requestBody.getPassword()))
                .build();

        return this.userRepository.save(newUser);
    }
}
