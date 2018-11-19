package com.isanuric.bar.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final static Logger logger = LoggerFactory.getLogger(ManagerController.class);

    //    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String appName = "ThymeleafTour";

    public ManagerController(
            BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @JsonView
    @GetMapping("/password")
//    public String password(@RequestBody User user) {
    public String password() {

        logger.debug("start password");

        String hashedPassword = bCryptPasswordEncoder.encode("123456");
//        user.setPassword(hashedPassword);
        logger.debug("pass: {}", hashedPassword);
//        applicationUserRepository.save(user);
        return hashedPassword;
    }

    @PostMapping("/login")
    public Authentication test() {
        logger.debug("start test");
        return SecurityContextHolder.getContext().getAuthentication();

    }

////    @JsonView
//    @GetMapping("/managers")
//    public CustomAuthenticationFilter managers(){
//        return SecurityContextHolder.getContext().getAuthentication();
//    }

//    @GetMapping("/employees")
//    public String employees(){
//        return "Hello employees";
//    }

//    @RequestMapping("/login")
//    public String login() {
//        return "home";
//    }

//    @PostMapping("/sign-up")
//    public void signUp(@RequestBody User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }


}
