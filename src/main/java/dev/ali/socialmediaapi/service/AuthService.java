package dev.ali.socialmediaapi.service;

import dev.ali.socialmediaapi.config.CustomAuthenticationProvider;
import dev.ali.socialmediaapi.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final CustomAuthenticationProvider authenticationProvider;
    private final JWTService jwtService;
    private final UserService userService;

    public AuthService( CustomAuthenticationProvider authenticationProvider, JWTService jwtService, UserService userService) {
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public void RegisterUser(String username, String displayName, String email, String password) {
        User user = new User(username, displayName, email, password);
        user.setPhotoUrl("https://i.ibb.co/VHZPVZ4/blank-profile-picture-973460-1920.png");
        userService.addUser(user);
    }

    public ResponseEntity<?> LoginUser(String email, String password) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationProvider.authenticate(credentials);
        String jwt = jwtService.getToken(auth.getName());

        if(auth.isAuthenticated()) {
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization").build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }


    }


}
