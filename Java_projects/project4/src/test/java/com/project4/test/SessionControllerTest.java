package com.project4.test;

import com.project4.config.SecurityConfig;
import com.project4.controllers.SessionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@Import(SecurityConfig.class)
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCurrentUser_asAuthenticatedUser_shouldReturnUsername() throws Exception {
        String username = "currentUser";
        
        UserDetails userDetails = User.withUsername(username)
                .password("password")
                .roles("USER")
                .build();

        mockMvc.perform(get("/api/v1/sessions/current")
                        .with(user(userDetails))) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    @WithMockUser(username = "anotherUser") 
    void getCurrentUser_asAuthenticatedUserWithAnnotation_shouldReturnUsername() throws Exception {
        mockMvc.perform(get("/api/v1/sessions/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("anotherUser"));
    }

    @Test
    void getCurrentUser_asUnauthenticated_shouldReturnUnauthorizedWithMessage() throws Exception {
        mockMvc.perform(get("/api/v1/sessions/current"))
                .andExpect(status().isUnauthorized()) 
                .andExpect(content().string("Not authenticated"));
    }
}