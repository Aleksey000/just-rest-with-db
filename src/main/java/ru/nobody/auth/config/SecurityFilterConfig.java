package ru.nobody.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.nobody.auth.config.filter.HttpOnlyCookieAuthenticationFilter;
import ru.nobody.auth.services.AuthenticationService;

import java.util.Enumeration;
import java.util.Iterator;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityFilterConfig {

    public static final String[] WHITE_LIST_URL = {
            "/v1/auth"
    };

    public static final String[] WHITE_LIST_URL_PATTERNS = {
            "/v1/auth/**"
    };

    private final HttpOnlyCookieAuthenticationFilter httpOnlyCookieAuthenticationFilter;

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationService authenticationService) throws Exception {
        return http
                .headers(headersConfigurer -> headersConfigurer.frameOptions(Customizer.withDefaults()).disable())
                .cors(Customizer.withDefaults()) // TODO
                .csrf(AbstractHttpConfigurer::disable) // TODO
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sessionConfigurer ->
                        sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(customer -> customer
                        .permitAll()
                        .logoutSuccessHandler((req, response, auth) -> {
                            authenticationService.logoutUser(response);
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().flush();
                        })
                )
                .authenticationProvider(daoAuthenticationProvider)
                .addFilterBefore(httpOnlyCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint((request, response, authException) -> {
                            log.error("Этой ошиби не должно быть", authException);
//                            // TODO just bad response with HTTP code
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        }))
                .build();
    }

    private String getHeader(HttpServletRequest request, String header ) {
        Enumeration<String> headers = request.getHeaders(header);
        Iterator<String> stringIterator = headers.asIterator();
        if (stringIterator.hasNext()) {
            return stringIterator.next();
        }
        return null;
    }

}