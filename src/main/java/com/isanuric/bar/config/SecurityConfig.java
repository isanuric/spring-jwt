package com.isanuric.bar.config;

import com.isanuric.bar.filter.JwtAuthenticationFilter;
import com.isanuric.bar.filter.JwtAuthorizationFilter;
import com.isanuric.bar.filter.JwtCsrfToken;
import com.isanuric.bar.ldap.UserService;
import com.isanuric.bar.service.CustomUserDetailsService;
import com.isanuric.bar.service.JwtService;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfFilter;

/*
 * Project: bar
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

    @Value("${jwt.expiration.time.days}")
    private long expirationTime;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)

                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()

                .authorizeRequests()
                .antMatchers("login", "index", "/error", "/unsecure/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new JwtCsrfToken(), CsrfFilter.class)
                .addFilter(jwtAuthenticationFilter())
                .addFilter(jwtAuthorizationFilter());
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
        JwtService jwtService = new JwtService();
        jwtService.setExpirationTime(Duration.ofHours(expirationTime).toHours());
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
    public UserService userService() {
        return new UserService(new BCryptPasswordEncoder());
    }


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
}
