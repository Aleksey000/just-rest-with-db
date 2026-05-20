package ru.nobody.rest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.nobody.auth.services.AuthenticationService;
import ru.nobody.data.entity.User;
import ru.nobody.data.enums.Role;
import ru.nobody.rest.dto.BaseRestResponse;
import ru.nobody.services.UserService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Setter(onMethod = @__({@Autowired}))
    private AuthenticationService authenticationService;
    @Setter(onMethod = @__({@Autowired}))
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto,
                                        HttpServletResponse response) {
        try {
            authenticationService.login(loginDto.getUsername(), loginDto.getPassword(), response);

            UserMeInfo userInfo = getUserInfo();
            if (userInfo != null) {
                return new ResponseEntity<>(userInfo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Login error", e);
            return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public BaseRestResponse login(@RequestBody RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setRole(Role.USER);
        user.setPassword(userService.encryptPassword(registerDto.getPassword()));

        try {
            userService.createUser(user);
        } catch (Exception ex) {
            log.error("Register error", ex);
            return BaseRestResponse.builder()
                    .status(BaseRestResponse.RestStatus.builder()
                            .result(BaseRestResponse.ResultEnum.ERROR)
                            .text(ex.getMessage())
                            .build())
                    .build();
        }

        return BaseRestResponse.builder()
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me() {
        UserMeInfo userInfo = getUserInfo();

        if (userInfo != null) {
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private UserMeInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof User user)) {
            return null;
        }

        return UserMeInfo.builder()
                .id(user.getId())
                .name(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserMeInfo {
        private UUID id;
        private String name;
        private String email;
        private Role role;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {
        private String username;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterDto {
        private String username;
        private String password;
    }
}
