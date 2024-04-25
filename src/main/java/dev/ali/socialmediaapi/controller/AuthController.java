package dev.ali.socialmediaapi.controller;

import dev.ali.socialmediaapi.dto.LoginRequest;
import dev.ali.socialmediaapi.dto.RegisterRequest;
import dev.ali.socialmediaapi.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody RegisterRequest registerRequest) {
        authService.RegisterUser(registerRequest.username(), registerRequest.email(), registerRequest.password());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        log.info("{} {}", loginRequest.email(), loginRequest.password());
        return authService.LoginUser(loginRequest.email(), loginRequest.password());
    }

}
