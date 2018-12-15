package com.isanuric.bar.filter;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/*
 * ----------------------------------------------
 * (c) 2018 Copyright iC Consult GmbH
 * <p/>
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 03/12/2018.
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e) throws IOException, ServletException {

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
