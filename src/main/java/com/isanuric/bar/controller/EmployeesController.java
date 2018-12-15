package com.isanuric.bar.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 16/11/2018.
 */
@RestController
//@RequestMapping("/employee")
public class EmployeesController {

    private final static Logger logger = LoggerFactory.getLogger(EmployeesController.class);



    @PostMapping("/login")
    public String login() { return "index"; }

//    @JsonView
    @GetMapping("/index")
    public String password() {

        logger.debug("start password");

        return "hallo";
    }
}
