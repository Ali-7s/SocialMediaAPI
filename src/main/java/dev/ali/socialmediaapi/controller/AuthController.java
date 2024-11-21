package dev.ali.socialmediaapi.controller;

import dev.ali.socialmediaapi.dto.LoginRequest;
import dev.ali.socialmediaapi.dto.RegisterRequest;
import dev.ali.socialmediaapi.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void registerUser(@Validated @RequestBody RegisterRequest registerRequest) {
        authService.registerUser(registerRequest.username(), registerRequest.displayName(), registerRequest.email(), registerRequest.password());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return authService.loginUser(loginRequest.email(), loginRequest.password());
    }

    @GetMapping("/refresh/{id}")
    public ResponseEntity<?> refreshAccessToken(@PathVariable Long id) {
        return authService.refreshAccessToken(id);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        return authService.logout(authentication.getName());
    }

}
