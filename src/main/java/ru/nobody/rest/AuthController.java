package ru.nobody.rest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nobody.auth.data.entity.User;
import ru.nobody.auth.services.AuthenticationService;

import java.util.Map;

@Slf4j
@RestController()
public class AuthController {

    @Setter(onMethod = @__({@Autowired}))
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> body,
                                        HttpServletResponse response) {
        try {
            // TODO DTO
            authenticationService.login(body.get("username"), body.get("password"), response);
            return new ResponseEntity<>("signed in", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Login error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        authenticationService.logoutUser(response);

        return new ResponseEntity<>("logout", HttpStatus.OK);
    }

    @GetMapping("/me")
    public TestController.UserMeInfo me() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        return TestController.UserMeInfo.builder()
                .id(user.getId())
                .name(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
