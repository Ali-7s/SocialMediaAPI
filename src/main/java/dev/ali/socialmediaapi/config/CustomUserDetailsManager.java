package dev.ali.socialmediaapi.config;

import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    public CustomUserDetailsManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        User actualUser = user.orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return new CustomUserDetails(actualUser);
    }

    @Override
    public void createUser(UserDetails user) {
        userRepository.save((User) user);
    }

    @Override
    public void updateUser(UserDetails user) {
        userRepository.save((User) user);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByEmail(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO: Implement change password.
    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
