package com.isanuric.bar.ldap;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * ----------------------------------------------
 * (c) 2018 Copyright iC Consult GmbH
 * <p/>
 * Project: bar
 * @author ehsan.salmani
 */
public class UserService implements UserDetailsService {

    public static final String TEST_USER_01 = "testUser01";
    private PasswordEncoder passwordEncoder;

    public UserService() {
    }

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals(TEST_USER_01)) {
            try {
                throw new AccessDeniedException("try again");
            } catch (AccessDeniedException e) {
                e.printStackTrace();
            }
        }

        return new User(
                TEST_USER_01,
                passwordEncoder.encode(TEST_USER_01),
                Collections.singleton(new SimpleGrantedAuthority("USER")));
    }

}
