package com.project4.controllers;

import com.project4.Dto.RegisterDto;
import com.project4.Dto.UserDto;
import com.project4.models.Owners;
import com.project4.models.Users;
import com.project4.repositories.OwnerRepository;
import com.project4.repositories.RoleRepository;
import com.project4.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Аутентификация", description = "Операции для входа и регистрации пользователей")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final OwnerRepository ownerRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            OwnerRepository ownerRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ownerRepository = ownerRepository;
    }

    @Operation(summary = "Вход пользователя в систему",
            description = "Аутентифицирует пользователя и создает сессию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(example = "Login successful"))),
            @ApiResponse(responseCode = "400", description = "Неверные учетные данные",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(example = "Invalid credentials"))) 
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Parameter(description = "Данные для входа (имя пользователя и пароль)", required = true)
            @RequestBody UserDto userDto, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.username(),
                        userDto.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        return ResponseEntity.ok("Login successful");
    }

    @Operation(summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя и связанного с ним владельца.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(example = "User registered"))),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким именем уже существует",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(example = "User already exists"))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера (например, роль не найдена)",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(example = "Role not found")))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Parameter(description = "Данные для регистрации (имя пользователя, пароль и имя владельца)", required = true)
                                              @RequestBody RegisterDto registerDto) {
        if (userRepository.findByUsername(registerDto.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        Users user = new Users();
        user.setUsername(registerDto.username());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        roleRepository.findByName("ROLE_USER")
                .ifPresentOrElse(
                        role -> user.setRoles(Set.of(role)),
                        () -> {
                            throw new RuntimeException("Role not found");
                        }
                );
        userRepository.save(user);

        Owners owner = new Owners();
        owner.setName(registerDto.name());

        owner.setUser(user);
        user.setOwner(owner);

        ownerRepository.save(owner);


        return ResponseEntity.ok("User registered");
    }

}
