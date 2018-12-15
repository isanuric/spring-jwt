package com.isanuric.bar.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/*
 * Project: bar
 * @author ehsan.salmani
 */
public class JwtCsrfToken extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtCsrfToken.class);

    private static final Set<String> SAFE_METHODS = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));
    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (csrfTokenIsRequired(request)) {
            String csrfHeaderToken = request.getHeader("X-CSRF-TOKEN");
            logger.debug("csrfHeaderToken: {}", csrfHeaderToken);

            String csrfCookieToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Optional<Cookie> csrfCookie = Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals("CSRF-TOKEN"))
                        .findFirst();
                if (csrfCookie.isPresent()) {
                    csrfCookieToken = csrfCookie.get().getValue();
                }
            }

            if (ObjectUtils.isEmpty(csrfHeaderToken)||
                    ObjectUtils.isEmpty(csrfCookieToken) ||
                    !csrfCookieToken.equals(csrfHeaderToken)) {

                accessDeniedHandler.handle(
                        request,
                        response,
                        new AccessDeniedException("CSRF tokens missing or not matching"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean csrfTokenIsRequired(HttpServletRequest request) {
        return !SAFE_METHODS.contains(request.getMethod());
    }

}
