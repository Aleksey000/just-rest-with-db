package ru.nobody.auth.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
//    private final PasswordEncoder passwordEncoder;
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

    // TODO
//    public void registerAccount(SignupRequest signupRequest){
//        if(accountRepository.existsByEmail(signupRequest.getEmail())){
//            throw new EntityExistsException("Email already used");
//        }
//        // create user object
//        Account account = new Account(signupRequest.getName(), signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()));
//        Role role = roleRepository.findByErole(ERole.ROLE_USER).orElse(null);
//        account.setRoles(Collections.singleton(role));
//        accountRepository.save(account);
//    }

    public void logoutUser(HttpServletResponse response) {
        Cookie cookie = accessTokenCookieService.createBlankHttpOnlyCookie();
        response.addCookie(cookie);
    }
}
