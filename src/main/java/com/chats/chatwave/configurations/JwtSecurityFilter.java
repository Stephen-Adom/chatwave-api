package com.chats.chatwave.configurations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chats.chatwave.model.Token;
import com.chats.chatwave.repository.TokenRepository;
import com.chats.chatwave.service.JwtService;
import com.chats.chatwave.service.UserService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtSecurityFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final UserService userDetailsService;

    private final TokenRepository tokenRepository;

    public JwtSecurityFilter(TokenRepository tokenRepository,
            UserService userDetailsService, JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                Optional<Token> tokenExist = this.tokenRepository.findByToken(token.split("\\.")[1]);

                if (tokenExist.isEmpty()) {

                }

                if (!tokenExist.get().getExpired() && !tokenExist.get().getRevoked()
                        && jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException exception) {
            handleExceptions(exception, response);
        } catch (SignatureException exception) {
            handleExceptions(exception, response);
        }

        filterChain.doFilter(request, response);
    }

    private void handleExceptions(Exception exception, HttpServletResponse response)
            throws StreamWriteException, DatabindException, IOException {
        response.setHeader("error", exception.getMessage());
        response.setStatus(401);
        Map<String, String> error = new HashMap<>();
        error.put("message", getClientErrorMessag(exception));
        error.put("status", HttpStatus.UNAUTHORIZED.name());
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    private String getClientErrorMessag(Exception exception) {
        if (exception instanceof ExpiredJwtException) {
            return "Token has Expired";
        } else if (exception instanceof SignatureException) {
            return "Invalid Token: Signature is invalid";
        }

        return "Token is Invalid";
    }
}
