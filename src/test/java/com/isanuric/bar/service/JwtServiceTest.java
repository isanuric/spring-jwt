package com.isanuric.bar.service;


import static org.assertj.core.api.Assertions.assertThat;

import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.lang.JoseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtServiceTest {

    public static final String TEST_USER_01 = "testUser01";

    @Autowired
    protected JwtService jwtService;

    @Test
    public void buildRequestBody() {
        String token = jwtService.createJwsToken(TEST_USER_01);
        assertThat(token).isNotBlank();
    }

    @Test
    public void validateToken() {
        JSONObject validatedToken = jwtService.verifyToken(jwtService.createJwsToken(TEST_USER_01));

        assertThat(validatedToken).isNotEmpty();
        System.out.println(validatedToken);
        assertThat(validatedToken.get("user")).isEqualTo(TEST_USER_01);
//        assertThat(validatedToken.get("exp"))
    }

    @Test
    public void encryptionAndDecryption() throws JoseException {

        String plainText = "encryptMe!";
        String cipherText = jwtService.doEncryption(plainText);
        assertThat(plainText.equals(jwtService.doDecryption(cipherText))).isTrue();

    }

}