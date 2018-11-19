package com.isanuric.bar.service;

import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 18/11/2018.
 */
public class CustomLdapUserService extends LdapUserDetailsService {


    public CustomLdapUserService(LdapUserSearch userSearch) {
        super(userSearch);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User applicationUser = userRepository.findByUsername(username);
//        if (applicationUser == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
//    }
}
