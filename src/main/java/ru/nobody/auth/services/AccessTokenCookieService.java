package ru.nobody.auth.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static ru.nobody.auth.Constants.COOKIE_EXPIRY;
import static ru.nobody.auth.Constants.COOKIE_NAME;

@Service
public class AccessTokenCookieService {

    /**
     * Получаем JWT токен
     */
    public Optional<Cookie> getAccessTokenCookie(final HttpServletRequest request) {
        return Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[]{}))
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findAny();
    }

    /**
     * Возвращаем куки с токеном
     */
    public Cookie createHttpOnlyCookie(final String cookieValue) {
        requireNonNull(cookieValue);

        final var cookie = new Cookie(COOKIE_NAME, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(COOKIE_EXPIRY);
        cookie.setPath("/");

        return cookie;
    }

    /**
     * Возвращаем куки с очищеным токеном
     */
    public Cookie createBlankHttpOnlyCookie() {
        final var cookie = new Cookie(COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(1);
        cookie.setPath("/");

        return cookie;
    }
}
