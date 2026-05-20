package ru.nobody.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nobody.data.entity.User;
import ru.nobody.data.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(User user) {
        String username = user.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new RuntimeException("Username must be not blank"); // TODO придумать классыне Exception
        }
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            throw new RuntimeException("User already exist"); // TODO придумать классыне Exception
        }
        userRepository.save(user);
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
