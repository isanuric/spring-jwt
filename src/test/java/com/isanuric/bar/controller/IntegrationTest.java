package com.isanuric.bar.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.isanuric.bar.config.SecurityConfig;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
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
        assertThat(authorizationValue.startsWith("Bearer eyJhbGciOi")).isTrue();
    }

    @Test
    public void errorEndpoint() {
        webTestClient.get().uri("/error")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult().getResponseBody().contains("error page");
    }

    @Test
    public void encrypt_unsecure() {
        EntityExchangeResult result = webTestClient.get().uri("/unsecure/encryption")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();
        System.out.println(result);

        // is token created?
        assertThat(result.getResponseBody().toString().startsWith("eyJhbGciOiJBMjU2S1")).isTrue();
    }

    // TODO: 17/07/2019 write more test using help methods
    // ~ Help methods
    // -----------------------------------------------------------------------------------------------------------------
    private String getCredentials(ClientHttpResponse response) {
        String credentials = null;
        List<String> authorizationTokens = response.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authorizationTokens != null && authorizationTokens.size() > 0) {
            credentials = authorizationTokens.get(0);
        }
        return credentials;
    }

    private static void setHeaders(ClientHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String csrfToken = UUID.randomUUID().toString();
        headers.set(HttpHeaders.COOKIE, SecurityConfig.CSRF_COOKIE + "=" + csrfToken);
        headers.set(SecurityConfig.CSRF_COOKIE, csrfToken);
    }

    private static void setBody(String username, String password, ClientHttpRequest request) throws IOException {
        OutputStream body = request.getBody();
        body.write(("username=" + username + "&password=" + password).getBytes());
        body.flush();
        body.close();
    }

    private String getToken(ClientHttpResponse response) {
        List<String> authorizationTokens = response.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authorizationTokens != null && authorizationTokens.size() > 0) {
            return authorizationTokens.get(0);
        }
        return null;
    }

}
