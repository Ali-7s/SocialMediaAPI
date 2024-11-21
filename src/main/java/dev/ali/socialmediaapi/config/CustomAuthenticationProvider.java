package dev.ali.socialmediaapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        try {
            UserDetails user = userDetailsManager.loadUserByUsername(username);
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                
                return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            } else {
                log.warn("Authentication failed for user '{}': Incorrect password.", username);
                throw new BadCredentialsException("Incorrect credentials.");
            }
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for user '{}': {}", username, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
