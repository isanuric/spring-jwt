package com.isanuric.bar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Project: bar
 * @author ehsan.salmani
 */

@RestController
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public String login() { return "index"; }

    @GetMapping("/index")
    public String password() {
        logger.debug("start password");
        return "hallo";
    }

    @GetMapping("/one")
    public String secureOne() { return "secure one.";}

    @GetMapping("/error")
    public String errorPage() { return "error page.";}
}
