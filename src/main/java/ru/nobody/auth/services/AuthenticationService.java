package ru.nobody.auth.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.nobody.data.entity.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final AccessTokenCookieService accessTokenCookieService;

    public void login(String username, String password, HttpServletResponse response) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        String token = jwtTokenService.generateToken(username);
        Cookie authCookie = accessTokenCookieService.createHttpOnlyCookie(token);
        response.addCookie(authCookie);
    }

    public void logoutUser(HttpServletResponse response) {
        Cookie cookie = accessTokenCookieService.createBlankHttpOnlyCookie();
        response.addCookie(cookie);
    }

    static public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User user) {
            return user;
        }
        throw new RuntimeException("User not found"); // TODO
    }
}
