package com.isanuric.bar;

import com.isanuric.bar.service.JwtService;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 17/11/2018.
 */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(initializers = PortInitializer.class)

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = { "cwl.locked_max_age=250"})
public class TestBase {

//    @Autowired
//    protected WebTestClient webTestClient;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected JwtService jwtService;

    public String TESTUSER_01 = "ben";
    public String TESTPASS_01 = "benspassword";

}
