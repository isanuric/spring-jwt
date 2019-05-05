package com.isanuric.bar.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

import com.isanuric.bar.TestBase;
import com.isanuric.bar.config.SecurityConfig;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/*
 * Project: spring-jwt-ldap
 * @author ehsan.salmani
 */
public class IntegrationTest extends TestBase {

    @Autowired
    protected WebTestClient webTestClient;

    @LocalServerPort
    int randomServerPort;

    public static final String TEST_USER_01 = "testUser01";


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

        // expect authorization token
        String authorizationValue = response.getResponseHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        assertThat(authorizationValue.startsWith("Bearer eyJhbGciOiJIU"));

        // use created jwt token to access the controller
        webTestClient.get().uri("/index")
                .header(HttpHeaders.AUTHORIZATION, authorizationValue)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult().getResponseBody().contains("hallo");
    }

    @Test
    public void secureOne() {
        webTestClient.get().uri("/one")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(String.class).returnResult().getResponseBody().contains("secure one.");
    }

    @Test
    public void errorEndpoint() {
        webTestClient.get().uri("/error")
                .exchange()
                .expectStatus().isOk()
        .expectBody(String.class).returnResult().getResponseBody().contains("error page");
    }

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
        headers.set(SecurityConfig.CSRF_HEADER, csrfToken);
    }

    private static void setBody(String username, String password, ClientHttpRequest request) throws IOException {
        OutputStream body = request.getBody();
        body.write(("username=" + username + "&password=" + password).getBytes());
        body.flush();
        body.close();
    }

    /**
     * Execute the HTTP method to the given URI template, writing the given request entity to the request, and returns
     * the response as {@link ResponseEntity}.
     */
    private <T> ResponseEntity<T> executeHttpGetMethod(String entryPoint, String token, Class<T> responseType)
            throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();

        // add created jwt to authorization header
        headers.add(HttpHeaders.AUTHORIZATION, token);
        URI uri = new URI("http://localhost:" + randomServerPort + "/index/");

        return testRestTemplate.exchange(uri, GET, new HttpEntity<>(headers), responseType);
    }

    private String getToken(ClientHttpResponse response) {
        List<String> authorizationTokens = response.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authorizationTokens != null && authorizationTokens.size() > 0) {
            return authorizationTokens.get(0);
        }
        return null;
    }
}
