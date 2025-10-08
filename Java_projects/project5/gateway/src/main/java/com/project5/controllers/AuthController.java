package com.project5.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project5.config.RabbitMQConfig;
import com.project5.dto.OwnerDto;
import com.project5.dto.RegisterDto;
import com.project5.models.Users;
import com.project5.repositories.RoleRepository;
import com.project5.repositories.UserRepository;
import com.project5.services.MessagingGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException; 
import java.util.concurrent.CompletableFuture; 

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Аутентификация", description = "Операции для входа и регистрации пользователей")
public class AuthController {
    
    private final UserRepository userRepository; 
    private final RoleRepository roleRepository; 
    private final PasswordEncoder passwordEncoder; 

    private final MessagingGateway messagingGateway; 
    private final ObjectMapper objectMapper; 

    @Autowired
    public AuthController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            MessagingGateway messagingGateway,
            ObjectMapper objectMapper 
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.messagingGateway = messagingGateway;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Parameter(description = "Данные для регистрации (имя пользователя, пароль и имя владельца)", required = true)
            @RequestBody RegisterDto registerDto) throws InterruptedException { 

        if (userRepository.findByUsername(registerDto.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с таким именем уже существует!");
        }

        Users user = new Users();
        user.setUsername(registerDto.username());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        roleRepository.findByName("ROLE_USER")
                .ifPresentOrElse(
                        role -> user.setRoles(Set.of(role)),
                        () -> {
                            throw new RuntimeException("Роль 'ROLE_USER' не найдена в базе данных!");
                        }
                );
        Users savedUser = userRepository.save(user); 

        try {
            Map<String, Object> ownerData = new HashMap<>();
            ownerData.put("name", registerDto.name());
            ownerData.put("id", savedUser.getId());
            OwnerDto createdOwner = messagingGateway.sendOwnerRequest(
                    RabbitMQConfig.OWNER_CREATE_KEY,
                    ownerData,
                    OwnerDto.class
            ).get(); 

            if (createdOwner == null) {

                userRepository.delete(savedUser);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Пользователь зарегистрирован, но не удалось создать соответствующего владельца. Откат.");
            }

            
            return ResponseEntity.ok("Пользователь зарегистрирован и владелец создан успешно!");

        } catch (ExecutionException e) {
            
            userRepository.delete(savedUser); 
            Throwable cause = e.getCause(); 
            if (cause instanceof ResponseStatusException) {
                
                throw (ResponseStatusException) cause;
            }
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка при создании владельца: " + (cause != null ? cause.getMessage() : e.getMessage()) + ". Откат регистрации пользователя.");
        } catch (InterruptedException e) {
            
            Thread.currentThread().interrupt(); 
            userRepository.delete(savedUser); 
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Процесс регистрации прерван. Откат регистрации пользователя.");
        } catch (Exception e) {
            
            userRepository.delete(savedUser); 
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Произошла непредвиденная ошибка во время регистрации: " + e.getMessage() + ". Откат регистрации пользователя.");
        }
    }

}