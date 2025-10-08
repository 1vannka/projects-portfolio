package com.project4.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project4.Dto.RegisterDto;
import com.project4.Dto.UserDto;
import com.project4.config.SecurityConfig;
import com.project4.controllers.AuthController;
import com.project4.models.Owners;
import com.project4.models.Role; 
import com.project4.models.Users;
import com.project4.repositories.OwnerRepository;
import com.project4.repositories.RoleRepository;
import com.project4.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private OwnerRepository ownerRepository;


    @Test
    void loginWithValidCredentials_shouldReturnOk() throws Exception {
        UserDto userDto = new UserDto("user1", "123");
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(userDto.username(), userDto.password(), Collections.emptyList());

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"))
                .andReturn();

        Object securityContext = result.getRequest().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        assertNotNull(securityContext);
        assertTrue(securityContext instanceof SecurityContextImpl);
        assertEquals(mockAuthentication, ((SecurityContextImpl) securityContext).getAuthentication());
    }

    @Test
    void loginWithInvalidCredentials_shouldReturnUnauthorized() throws Exception {
        UserDto userDto = new UserDto("user2", "321");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isUnauthorized()); 
    }

    @Test
    void registerUser_shouldReturnOk() throws Exception {
        RegisterDto registerDto = new RegisterDto("user", "123", "Арбуз");
        Role userRole = new Role(); userRole.setName("ROLE_USER");
        Users newUser = new Users(); newUser.setUsername(registerDto.username());
        Owners newOwner = new Owners(); newOwner.setName(registerDto.name());

        when(userRepository.findByUsername(registerDto.username())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerDto.password())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(Users.class))).thenReturn(newUser);
        when(ownerRepository.save(any(Owners.class))).thenReturn(newOwner);


        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered"));
    }

    @Test
    void logout_shouldReturnOkAndInvalidateSession() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .with(user("user5").roles("USER"))
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"))
                .andDo(result -> {
                    assertNull(result.getRequest().getSession(false)); 
                });
    }
}