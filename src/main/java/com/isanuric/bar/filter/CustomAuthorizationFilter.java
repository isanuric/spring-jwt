package com.isanuric.bar.filter;


import com.isanuric.bar.service.JwtService;
import com.isanuric.bar.utils.Const;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 17/11/2018.
 */
public class CustomAuthorizationFilter extends BasicAuthenticationFilter {

    private final static Logger logger = LoggerFactory.getLogger(CustomAuthorizationFilter.class);

    @Autowired
    private JwtService jwtService;

    public CustomAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        String headerAuthentication = request.getHeader(Const.HEADER_AUTHORIZATION);

        if (StringUtils.isEmpty(headerAuthentication) || !headerAuthentication.startsWith(Const.TOKEN_PREFIX)) {
            logger.warn("headerAuthentication error [{}]", headerAuthentication);
            filterChain.doFilter(request, response);
            return;
        }

        logger.debug("headerAuthentication: {}", headerAuthentication);
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(request));
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String jws = request.getHeader(Const.HEADER_AUTHORIZATION);
        if (StringUtils.isEmpty(jws)) {
            logger.debug("token is empty");
            return null;
        }

        String verifiedToken = jwtService.verifyToken(jws).toJSONString();
        if (StringUtils.isEmpty(verifiedToken)) {
            throw new CredentialsExpiredException("token is invalid");
        }
        return new UsernamePasswordAuthenticationToken(verifiedToken, null, new ArrayList<>());
    }
}
