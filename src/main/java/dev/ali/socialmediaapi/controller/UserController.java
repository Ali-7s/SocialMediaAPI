package dev.ali.socialmediaapi.controller;

import dev.ali.socialmediaapi.dto.UpdateUserProfileRequest;
import dev.ali.socialmediaapi.dto.UserProfileDTO;
import dev.ali.socialmediaapi.dto.UserSummaryDTO;
import dev.ali.socialmediaapi.model.ApiResponse;
import dev.ali.socialmediaapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static dev.ali.socialmediaapi.utils.RequestUtils.getResponse;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getAuthenticatedUser(Authentication authentication) {
        UserProfileDTO userProfile = userService.getUserProfileByEmail(authentication.getName());
        return new ResponseEntity<>(getResponse(Map.of("user", userProfile), "Authenticated user retrieved."), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserProfileDTO> users = userService.findAllUsers();

        return new ResponseEntity<>(getResponse(Map.of("users", users), "All users successfully retrieved."), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        UserProfileDTO userProfile = userService.getUserProfile(id);
        return new ResponseEntity<>(getResponse(Map.of("user", userProfile), "User successfully retrieved."), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse> getUserByUsername(@PathVariable String username) {
        UserProfileDTO userProfile = userService.getUserProfileByUsername(username);
        return new ResponseEntity<>(getResponse(Map.of("user", userProfile), "User successfully retrieved."), HttpStatus.OK);
    }

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<ApiResponse> followUser(@PathVariable Long followingId, Authentication authentication) {
        UserProfileDTO follower = userService.getUserProfileByEmail(authentication.getName());
        userService.followUser(follower.getId(), followingId);
        return new ResponseEntity<>(getResponse(Collections.emptyMap(), "User successfully followed."), HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/{followingId}")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable Long followingId, Authentication authentication) {
        UserProfileDTO follower = userService.getUserProfileByEmail(authentication.getName());
        userService.unfollowUser(follower.getId(), followingId);
        return new ResponseEntity<>(getResponse(Collections.emptyMap(), "User successfully unfollowed."), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<ApiResponse> getFollowers(@PathVariable Long id) {
        List<UserSummaryDTO> followers = userService.getFollowers(id);
        return new ResponseEntity<>(getResponse(Map.of("followers", followers), "User followers successfully retrieved."), HttpStatus.OK);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<ApiResponse> getFollowing(@PathVariable Long id) {
        List<UserSummaryDTO> following = userService.getFollowing(id);
        return new ResponseEntity<>(getResponse(Map.of("following", following), "User following successfully retrieved."), HttpStatus.OK);
    }

    @PostMapping(path = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadPhoto(Authentication authentication, @RequestPart("file") MultipartFile file) {
        UserProfileDTO userProfile = userService.getUserProfileByEmail(authentication.getName());
        userService.uploadPhoto(userProfile.getId(), file);
        return new ResponseEntity<>(getResponse(Collections.emptyMap(), "User profile photo uploaded."), HttpStatus.OK);
    }

    @GetMapping("/photo")
    public ResponseEntity<ApiResponse> getUserPhoto(Authentication authentication) {
        UserProfileDTO userProfile = userService.getUserProfileByEmail(authentication.getName());
        String photoUrl = userService.getImgUrl(userProfile.getId());
        return new ResponseEntity<>(getResponse(Map.of("photoUrl", photoUrl), "User profile photo retrieved."), HttpStatus.OK);
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<ApiResponse> getUserPhoto(@PathVariable Long id) {
        String photoUrl = userService.getImgUrl(id);
        return new ResponseEntity<>(getResponse(Map.of("profile_img", photoUrl), "User profile photo retrieved."), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse> updateUserProfile(Authentication authentication, @RequestBody UpdateUserProfileRequest userProfile) {
        UserProfileDTO updatedProfile = userService.updateUser(authentication.getName(), userProfile.username(), userProfile.displayName());
        return new ResponseEntity<>(getResponse(Map.of("profile", updatedProfile), "User profile successfully updated."), HttpStatus.OK);
    }
}
