package com.isanuric.bar.controller;

import com.isanuric.bar.service.JwtService;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Project: bar
 * @author ehsan.salmani
 */

@RestController
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private JwtService jwtService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/one")
    public String secureOne() { return "hallo secure.";}


    @GetMapping("/error")
    public String errorPage() {
        return "error page.";
    }

    @GetMapping("/secure")
    public String encrypt() {
        try {
            return jwtService.doEncryption("textThatShouldBeEncrypted");
        } catch (JoseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
