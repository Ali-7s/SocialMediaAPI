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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationProvider authenticationProvider;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomAuthenticationProvider authenticationProvider, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
    }

    public void RegisterUser(String username, String email, String password) {
        User user = new User(username, email, passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public ResponseEntity<?> LoginUser(String email, String password) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationProvider.authenticate(credentials);
        log.info(String.valueOf(credentials));
        String jwt = jwtService.getToken(auth.getName());

        if(auth.isAuthenticated()) {
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization").build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }


    }


}
