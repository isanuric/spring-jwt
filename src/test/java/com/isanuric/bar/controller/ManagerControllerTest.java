package com.isanuric.bar.controller;

import com.isanuric.bar.TestBase;
import com.isanuric.bar.ldap.User;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/*
 * ----------------------------------------------
 * (c) 2018 Copyright iC Consult GmbH
 * <p/>
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 18/11/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class ManagerControllerTest extends TestBase {


    @Autowired
    public TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testAddEmployeeSuccess() throws URISyntaxException {

        final String baseUrl = new StringBuilder()
                .append("http://localhost:").append(randomServerPort).append("/login")
                .toString();

        URI uri = new URI(baseUrl);
        User employee = new User( TESTUSER_01, TESTPASS_01, "testuid01");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<User> request = new HttpEntity<>(employee, headers);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity(uri, request, String.class);

        //Verify request succeed
        Assert.assertEquals(201, result.getStatusCodeValue());
    }
}