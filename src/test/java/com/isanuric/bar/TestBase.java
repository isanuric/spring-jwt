package com.isanuric.bar;

import com.isanuric.bar.service.JwtService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 17/11/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class TestBase {

    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    public JwtService jwtService;

    public String TESTUSER_01 = "ben";
    public String TESTPASS_01 = "benspassword";

}
