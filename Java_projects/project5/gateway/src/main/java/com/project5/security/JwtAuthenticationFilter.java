package com.project5.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project5.dto.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, ObjectMapper objectMapper) {
        super.setAuthenticationManager(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/api/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, String>>() {
            });
            String username = credentials.get("username");
            String password = credentials.get("password");

            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении запроса аутентификации", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final UserDetails userDetails = (UserDetails) authResult.getPrincipal(); 

        String token = jwtTokenUtil.generateToken(userDetails); 

        response.setContentType("application/json");
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("token", token);
        response.addHeader("Authorization", "Bearer " + token); 
        objectMapper.writeValue(response.getWriter(), tokenResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Аутентификация не удалась");
        errorResponse.put("message", failed.getMessage());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}