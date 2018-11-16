//package com.isanuric.bar.filter;
//
//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//
///*
// * ----------------------------------------------
// * (c) 2018 Copyright iC Consult GmbH
// * <p/>
// * Project: bar
// * Created by ehsan.salmani@ic-consult.de on 11/11/2018.
// */
//public class JwtAuthentication extends AbstractAuthenticationProcessingFilter {
//
//    protected JwtAuthentication(String defaultFilterProcessesUrl) {
//        super(defaultFilterProcessesUrl);
//    }
//
//    @Override
//    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        return true;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(
//            HttpServletRequest httpServletRequest,
//            HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
//
//        String header = httpServletRequest.getHeader("Authorization");
//
////        if (header == null || !header.startsWith("Bearer ")) {
////            throw new JwtTokenMissingException("No JWT token found in request headers");
////        }
////
////        String authToken = header.substring(7);
////
////        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);
////
////        return getAuthenticationManager().authenticate(authRequest);
//        return null;
//    }
//}
