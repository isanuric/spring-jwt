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
import org.apache.commons.lang.StringUtils;
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

    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // call next filter to handle authentication/authorization.
        if (StringUtils.containsAny(request.getMethod(), String.valueOf(Arrays.asList("POST", "DELETE", "PUT")))) {
            filterChain.doFilter(request, response);
            return;
        }

        // check if the csrf token is set.
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

        checkCsrf(request, response, csrfHeaderToken, csrfCookieToken);
        filterChain.doFilter(request, response);
    }

    private void checkCsrf(
            HttpServletRequest request,
            HttpServletResponse response,
            String csrfHeaderToken,
            String csrfCookieToken) throws IOException, ServletException {

        if (ObjectUtils.isEmpty(csrfHeaderToken) || ObjectUtils.isEmpty(csrfCookieToken) ) {
            handleAccessDenied(request, response, "CSRF tokens empty.");
        }

        if (!csrfCookieToken.equals(csrfHeaderToken)) {
            handleAccessDenied(request, response, "CSRF tokens not matching.");
        }
    }

    private void handleAccessDenied(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException, ServletException {
        accessDeniedHandler.handle(
                request,
                response,
                new AccessDeniedException(message));
    }
}
