package com.isanuric.bar.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import com.isanuric.bar.TestBase;
import com.isanuric.bar.config.SecurityConfig;
import com.isanuric.bar.ldap.UserService;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

/*
 * Project: bar
 * @author ehsan.salmani
 */
public class IntegrationTest extends TestBase {

    @LocalServerPort
    int randomServerPort;

    @Test
    public void createAndUseJwtToken_success() throws URISyntaxException {

        // creates jwt token
        String credentials = login(UserService.TEST_USER_01, UserService.TEST_USER_01);

        // use created jwt token to access
        ResponseEntity<String> response = executeHttpGetMethod("/index", credentials, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualToIgnoringCase("hallo");
    }

    public String login(String username, String password) throws URISyntaxException {
        return testRestTemplate.execute(
                new URI("http://localhost:" + randomServerPort + "/login"),
                POST,
                request -> {
                    setHeaders(request);
                    setBody(username, password, request);
                },
                response -> getCredentials(response)
        );
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
     * Execute the HTTP method to the given URI template, writing the given request entity
     * to the request, and returns the response as {@link ResponseEntity}.
     * @param entryPoint
     * @param token
     * @param responseType
     * @param <T>
     * @return
     * @throws URISyntaxException
     */
    private  <T> ResponseEntity<T> executeHttpGetMethod(String entryPoint, String token, Class<T> responseType) throws URISyntaxException {

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
