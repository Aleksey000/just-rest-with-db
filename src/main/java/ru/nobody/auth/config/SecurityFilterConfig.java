package ru.nobody.auth.config;

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

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityFilterConfig {

    private static final String[] WHITE_LIST_URL = {
            "login"
    };

    private final HttpOnlyCookieAuthenticationFilter httpOnlyCookieAuthenticationFilter;

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .headers(headersConfigurer -> headersConfigurer.frameOptions(Customizer.withDefaults()).disable())
                .cors(AbstractHttpConfigurer::disable) // TODO
                .csrf(AbstractHttpConfigurer::disable) // TODO
                .authorizeHttpRequests(request -> request
                        .requestMatchers(WHITE_LIST_URL)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sessionConfigurer ->
                        sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider)
                .addFilterBefore(httpOnlyCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint((request, response, authException) -> {
                            log.error("Этой ошиби не должно быть", authException);
                            // TODO just bad response with HTTP code
                            response.setStatus(HttpStatus.FOUND.value());
                            response.setHeader("Location", "/login");
                        }))
                .build();
    }
}