package com.isanuric.bar.config;

import com.isanuric.bar.filter.JwtAuthenticationFilter;
import com.isanuric.bar.filter.JwtAuthorizationFilter;
import com.isanuric.bar.filter.JwtCsrfToken;
import com.isanuric.bar.ldap.UserService;
import com.isanuric.bar.service.CustomUserDetailsService;
import com.isanuric.bar.service.JwtService;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfFilter;

/*
 * Project: bar
 * Created by ehsan.salmani@ic-consult.de on 06/11/2018.
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String CSRF_COOKIE = "CSRF-TOKEN";
    public static final String CSRF_HEADER = "X-CSRF-TOKEN";

//    @Autowired
//    private JwtAuthenticationEntryPoint unauthorizedHandler;

//    @Resource
//    private LdapUserDetailsService ldapUserDetailsService;

//    @Resource
//    private LdapUserDetailsService ldapUserDetailsService;


    @Resource
    private UserService userService;

    @Resource
    private CustomUserDetailsService customUserDetailsService;


    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http
//                .authorizeRequests()
//                    .antMatchers("/manager/**").hasRole("MANAGERS")
//                    .antMatchers("/employee/**").hasRole("EMPLOYEES")
//                    .anyRequest().fullyAuthenticated()
//
//                .and()
//                    .formLogin();

//        http
//                .cors().disable()
//                .csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(unauthorizedHandler)
//
//                .and()
//                .authorizeRequests()
////                .antMatchers(HttpMethod.POST, "/login").permitAll()
//                .antMatchers("/login").permitAll()
//                .anyRequest().authenticated()
//
//                .and()
//                // Add our custom JWT security filter
////                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilter(new CustomAuthenticationFilter(authenticationManager()))
//                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
//
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)

                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeRequests()
//                .antMatchers( "/index", "/login", "/error").permitAll()
                .antMatchers(  "/error").permitAll()
//                .anyRequest().authenticated()
                .antMatchers("/**").authenticated()


                .and()
                .addFilterBefore(new JwtCsrfToken(), CsrfFilter.class)
                .addFilter(jwtAuthenticationFilter())
                .addFilter(jwtAuthorizationFilter());

//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    // ~ Beans
    // -----------------------------------------------------------------------------------------------------------------
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() throws NoSuchAlgorithmException {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public TestRestTemplate restTemplate() {
        return new TestRestTemplate();
    }

    @Bean
    public JwtAuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public UserService userService () { return new UserService(new BCryptPasswordEncoder()); }


    private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setJwtService(jwtService());
        return jwtAuthenticationFilter;
    }

    private JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager());
        jwtAuthorizationFilter.setJwtService(jwtService());
        return jwtAuthorizationFilter;
    }







//    @Bean
//    public CustomUserDetailsService ldapUserDetailsService() {
//        return new CustomUserDetailsService(passwordEncoder);
//    }
//
//    @Resource
//    private PasswordEncoder passwordEncoder;

//    /**
//     * Custom ldap implementation of UserDetailsService
//     */
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .ldapAuthentication()
//                .userDnPatterns("uid={0},ou=people")
//                .groupSearchBase("ou=groups")
//                .contextSource()
//                .url("ldap://localhost:8389/dc=springframework,dc=org")
//
//                .and()
//                .passwordCompare()
//                .passwordEncoder(new LdapShaPasswordEncoder())
//                .passwordAttribute("userPassword");
//    }
//
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }
}
