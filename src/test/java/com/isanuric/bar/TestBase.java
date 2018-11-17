package com.isanuric.bar;

import com.isanuric.bar.service.JwtService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 17/11/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Import(MyTestsConfiguration.class)
public class TestBase {

    @Autowired
    public JwtService jwtService;


    // ~ test user
    // -----------------------------------------------------------------------------------------------------------------
    public String TESTUSER_01 = "testuser_01";

}
