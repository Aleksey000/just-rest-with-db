package ru.nobody.auth.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.nobody.auth.services.AccessTokenCookieService;
import ru.nobody.auth.services.JwtTokenService;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static ru.nobody.auth.config.SecurityFilterConfig.WHITE_LIST_URL;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpOnlyCookieAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    private final UserDetailsService userDetailsService;

    private final AccessTokenCookieService accessTokenCookieService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // TODO whiteList

        if (Objects.nonNull(request.getCookies()) || !inWhiteList(request)) {
            Optional<Cookie> accessTokenCookie = accessTokenCookieService.getAccessTokenCookie(request);

            accessTokenCookie.map(Cookie::getValue)
                    .filter(StringUtils::isNotBlank)
                    .ifPresentOrElse(cookieValue -> {
                        var username = jwtTokenService.extractUsername(cookieValue);

                        if (Objects.nonNull(username)) {
                            var authentication = SecurityContextHolder.getContext().getAuthentication();
                            if (Objects.nonNull(authentication)
                                    && !authentication.getName().equals(username))
                            {
                                SecurityContextHolder.clearContext();
                            }

                            Optional.ofNullable(userDetailsService.loadUserByUsername(username))
                                    .ifPresentOrElse(userDetails -> {
                                        if (jwtTokenService.validateToken(cookieValue, userDetails)) {
                                            var authToken = new UsernamePasswordAuthenticationToken(
                                                    userDetails,
                                                    null,
                                                    userDetails.getAuthorities()
                                            );
                                            authToken.setDetails(
                                                    new WebAuthenticationDetailsSource().buildDetails(request)
                                            );
                                            SecurityContextHolder.getContext().setAuthentication(authToken);
                                        }
                                    }, () -> {
                                        // user is removed from database or cookie is messed up for some reason, remove cookie data
                                        response.addCookie(accessTokenCookieService.createBlankHttpOnlyCookie());
                                    });
                        } else {
                            SecurityContextHolder.clearContext();
                        }
                    }, SecurityContextHolder::clearContext);
        }

        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, getHeader(request, HttpHeaders.ORIGIN));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS , "true");
        filterChain.doFilter(request, response);

        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, getHeader(request, HttpHeaders.ORIGIN));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS , "true");
//        response.setStatus(HttpStatus.OK.value());
//        response.getWriter().flush();
    }

    private boolean inWhiteList(HttpServletRequest request) {
        String path = request.getServletPath();
        return Stream.of(WHITE_LIST_URL).anyMatch(path::startsWith);
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