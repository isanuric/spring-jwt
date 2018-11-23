package com.isanuric.bar.service;


import com.isanuric.bar.TestBase;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.lang.JoseException;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 17/11/2018.
 */
public class JwtServiceTest extends TestBase {

    @Test
    public void buildRequestBody() {
        String token = jwtService.buildRequestToken(TESTUSER_01);
        assertThat(token).isNotBlank();
    }

    @Test
    public void validateToken() {
        JSONObject validatedToken = jwtService.verifyToken(jwtService.buildRequestToken(TESTUSER_01));

        assertThat(validatedToken).isNotEmpty();
        System.out.println(validatedToken);
        assertThat(validatedToken.get("user")).isEqualTo(TESTUSER_01);
//        assertThat(validatedToken.get("exp"))

    }
}