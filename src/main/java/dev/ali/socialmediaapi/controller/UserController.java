package dev.ali.socialmediaapi.controller;

import dev.ali.socialmediaapi.model.ApiResponse;
import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.Optional;
import static dev.ali.socialmediaapi.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/authed-user")
    public ResponseEntity<ApiResponse> getAuthedUser(Authentication user) {
        Optional<User> foundUser = userService.findByEmail(user.getName());
        return ResponseEntity.ok().body(getResponse(Map.of("user", foundUser), "Authenticated user retrieved", OK));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok().body(getResponse(Map.of("users", userService.findAllUsers()), "All users successfully retrieved.", OK));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<ApiResponse> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(getResponse(Map.of("user", userService.findById(id)), "User successfully retrieved.", OK));
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<ApiResponse> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(getResponse(Map.of("user", userService.findByUsername(username)), "User successfully retrieved.", OK));
    }
}
