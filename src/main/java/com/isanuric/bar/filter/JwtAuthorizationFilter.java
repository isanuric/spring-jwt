package com.isanuric.bar.filter;


import com.isanuric.bar.service.JwtService;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.debug("authorizationToken: {}", authorizationToken);

        if (authorizationToken == null || !authorizationToken.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // If user has an AUTHORIZATION JWS token, try to validate it.
        authorizationToken = authorizationToken.replaceFirst("Bearer ", "");
        String username = jwtService.verifyToken(authorizationToken).toJSONString();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList()));

        chain.doFilter(request, response);
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
