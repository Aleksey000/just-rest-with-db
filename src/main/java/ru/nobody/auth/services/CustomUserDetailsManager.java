package ru.nobody.auth.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.nobody.data.entity.User;
import ru.nobody.data.enums.Role;
import ru.nobody.data.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;

    @PostConstruct
    private void initAdmin() {
        if (userExists("admin")) {
            return;
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setUsername("admin");
        user.setRole(Role.ADMIN);
        user.setPassword(bCryptPasswordEncoder.encode("admin"));
        userRepository.save(user);
    }

    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException("TODO for Leha");
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("TODO for Leha");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("TODO for Leha");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("TODO for Leha");
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
    }
}
