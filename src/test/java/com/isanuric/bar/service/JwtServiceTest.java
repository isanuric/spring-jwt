package com.isanuric.bar.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.isanuric.bar.TestBase;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.junit.Test;

public class JwtServiceTest extends TestBase {

    @Test
    public void buildRequestBody() {
        String token = jwtService.createJwsToken(TESTUSER_01);
        assertThat(token).isNotBlank();
    }

    @Test
    public void validateToken() {
        JSONObject validatedToken = jwtService.verifyToken(jwtService.createJwsToken(TESTUSER_01));

        assertThat(validatedToken).isNotEmpty();
        System.out.println(validatedToken);
        assertThat(validatedToken.get("user")).isEqualTo(TESTUSER_01);
//        assertThat(validatedToken.get("exp"))

    }
}