package dev.ali.socialmediaapi.service;

import dev.ali.socialmediaapi.dto.UserMapper;
import dev.ali.socialmediaapi.dto.UserProfileDTO;
import dev.ali.socialmediaapi.dto.UserSummaryDTO;
import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(S3Service s3Service, UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.s3Service = s3Service;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;

    }

    public List<UserProfileDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserProfileDTO)
                .collect(Collectors.toList());
    }

    public UserProfileDTO getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id " + id));
        return userMapper.toUserProfileDTO(user);
    }

    public UserSummaryDTO getUserSummary(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id " + id));
        return userMapper.toUserSummaryDTO(user);
    }

    public UserProfileDTO getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));
        return userMapper.toUserProfileDTO(user);
    }

    public UserProfileDTO getUserProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
        return userMapper.toUserProfileDTO(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addUser(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername().toLowerCase());
        userRepository.save(user);
    }

    @Transactional
    public void followUser(Long followerId, Long followingId) {
        User followerUser = findUserById(followerId)
                .orElseThrow(() -> new UsernameNotFoundException("Follower not found"));
        User followingUser = findUserById(followingId)
                .orElseThrow(() -> new UsernameNotFoundException("User to follow not found"));
        followerUser.follow(followingUser);
        userRepository.save(followerUser);
        userRepository.save(followingUser);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        User followerUser = findUserById(followerId)
                .orElseThrow(() -> new UsernameNotFoundException("Follower not found"));
        User followingUser = findUserById(followingId)
                .orElseThrow(() -> new UsernameNotFoundException("User to unfollow not found"));
        followerUser.unfollow(followingUser);
        userRepository.save(followerUser);
    }

    public List<UserSummaryDTO> getFollowers(Long id) {
        User user = findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getFollowers().stream()
                .map(userMapper::toUserSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<UserSummaryDTO> getFollowing(Long id) {
        User user = findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getFollowing().stream()
                .map(userMapper::toUserSummaryDTO)
                .collect(Collectors.toList());
    }

    public void uploadPhoto(Long id, MultipartFile file) {
        String filename = id + "/user_profile" + getFileExtension(file.getOriginalFilename());
        try {

            s3Service.uploadFile(filename, file);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save image", e);
        }
    }

    public String getImgUrl(Long id) {
        String filename = id + "/user_profile." + "jpeg";
        return s3Service.getImgUrl(filename);
    }

    private String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(name -> name.contains("."))
                .map(name -> name.substring(name.lastIndexOf(".")))
                .orElse(".jpeg");
    }

    public UserProfileDTO updateUser(String email, String username, String displayName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(username.toLowerCase());
        user.setDisplayName(displayName);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserProfileDTO(updatedUser);
    }

    public void storeRefreshToken(String email, String refreshToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    public boolean isRefreshTokenValid(String email, String refreshToken) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String storedRefreshToken = user.getRefreshToken();
            return Objects.equals(refreshToken, storedRefreshToken);
        } else {

            return false;
        }
    }

    public void removeRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
        user.setRefreshToken(null);
        userRepository.save(user);
    }


}

