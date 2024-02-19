package com.chats.chatwave.configurations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.chats.chatwave.model.Token;
import com.chats.chatwave.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setHeader("error", "Token not available");
            response.setStatus(401);
            return;
        }

        token = authHeader.substring(7);
        String tokenPayload = token.split("\\.")[1];
        Optional<Token> storedToken = this.tokenRepository.findByToken(tokenPayload);

        if (storedToken.isPresent()) {
            Token tokenExist = storedToken.get();

            tokenExist.setExpired(true);
            tokenExist.setRevoked(true);
            this.tokenRepository.save(tokenExist);
        }

    }

}
