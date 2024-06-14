package dev.ali.socialmediaapi.service;

import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void addUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername().toLowerCase());
        userRepository.save(user);
    }

    @Transactional
    public void followUser(Long followerId, Long followingId) {
        User followerUser = findById(followerId).orElseThrow();
        User followingUser = findById(followingId).orElseThrow();
        followerUser.follow(followingUser);
        userRepository.save(followerUser);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        User followerUser = findById(followerId).orElseThrow();
        User followingUser = findById(followingId).orElseThrow();
        followerUser.unfollow(followingUser);
        userRepository.save(followerUser);
    }

    public Set<User> getFollowers(Long id) {
        User user = findById(id).orElseThrow();
        return user.getFollowers();
    }

    public Set<User> getFollowing(Long id) {
        User user = findById(id).orElseThrow();
        return user.getFollowing();
    }
}
