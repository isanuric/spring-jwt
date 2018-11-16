package com.isanuric.bar.controller;

//import com.isanuric.bar.user.User;
//import com.isanuric.bar.user.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
//@RequestMapping("/start")
public class LoginController {

    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    //    private UserRepository userRepository;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String appName = "ThymeleafTour";

//    public LoginController(
//            UserRepository userRepository,
//            BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.userRepository = userRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }

    @GetMapping("/")
    public String welcome() {
        return "home";
    }

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
