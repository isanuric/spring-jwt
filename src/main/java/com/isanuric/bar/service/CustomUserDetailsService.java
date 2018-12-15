package com.isanuric.bar.service;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
 * Project: bar
 * @author ehsan salmani
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService() {
    }

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.debug("UserDetails");
        if (!username.equals("user")) {
            throw new UsernameNotFoundException("not found");
        }

        return new User(
                "user",
                passwordEncoder.encode("password"),
                Collections.singleton(new SimpleGrantedAuthority("USER")));
    }
}
