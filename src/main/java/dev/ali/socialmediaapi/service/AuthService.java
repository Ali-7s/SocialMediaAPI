package dev.ali.socialmediaapi.service;

import dev.ali.socialmediaapi.config.CustomAuthenticationProvider;
import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {
    private final CustomAuthenticationProvider authenticationProvider;
    private final JWTService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthService(CustomAuthenticationProvider authenticationProvider, JWTService jwtService, UserService userService, UserRepository userRepository) {
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public void registerUser(String username, String displayName, String email, String password) {
        User user = new User(username, displayName, email, password);
        userService.addUser(user);
    }

    public ResponseEntity<?> loginUser(String email, String password) {

        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationProvider.authenticate(credentials);


        if (auth.isAuthenticated()) {
            String accessToken = jwtService.generateAccessToken(auth.getName());
            String refreshToken = jwtService.generateRefreshToken(auth.getName());

            userService.storeRefreshToken(auth.getName(), refreshToken);

            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization").build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    public ResponseEntity<?> refreshAccessToken(Long id) {
        User user = userService.findUserById(id).orElseThrow();
        if (jwtService.validateRefreshToken(user.getEmail(), user.getRefreshToken())) {
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateAccessToken(user.getEmail())).header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization").build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    public ResponseEntity<?> logout(String email) {
        User user = userService.findUserByEmail(email).orElseThrow();
        user.setRefreshToken(null);
        userRepository.save(user);
        return ResponseEntity.ok().body("Successfully Logged Out");
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();

    }

}
