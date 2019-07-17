package com.isanuric.bar.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.isanuric.bar.config.SecurityConfig;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/*
 * Project: spring-jwt-ldap
 * @author ehsan.salmani
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    protected WebTestClient webTestClient;

    @LocalServerPort
    int randomServerPort;

    private static final String TEST_USER_01 = "testUser01";

    @Test
    public void get_index() {
        EntityExchangeResult result = webTestClient.get().uri("/index")
                .exchange()

                .expectStatus().isOk()
                .expectBody(String.class).returnResult();

        assertThat(result.getResponseBody().equals("index")).isTrue();
    }

    @Test
    public void get_secure() {
        EntityExchangeResult result = webTestClient.get().uri("/one")
                .exchange()

                .expectStatus().isUnauthorized()
                .expectBody(String.class).returnResult();
        assertThat(result.getResponseBody().equals("error page.")).isTrue();
    }

    @Test
    public void createAndUseTokenToAccessController_success() {

        // creates token
        String csrfToken = UUID.randomUUID().toString();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", TEST_USER_01);
        formData.add("password", TEST_USER_01);

        EntityExchangeResult<String> response = webTestClient.post().uri("/login")
                .header(SecurityConfig.CSRF_COOKIE, csrfToken)
                .syncBody(formData)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();

        // expect authorization token is created
        String authorizationValue = response.getResponseHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        assertThat(authorizationValue.startsWith("Bearer qqqq")).isTrue();
    }

}
