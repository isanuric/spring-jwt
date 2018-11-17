package com.isanuric.bar.service;


import com.isanuric.bar.TestBase;
import org.jose4j.lang.JoseException;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 17/11/2018.
 */
public class JwtServiceTest extends TestBase {

    @Test
    public void buildRequestBody() throws JoseException {
        String token = jwtService.buildRequestToken(TESTUSER_01);
        assertThat(token).isNotBlank();
    }
}