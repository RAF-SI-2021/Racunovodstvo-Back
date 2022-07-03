package raf.si.racunovodstvo.knjizenje.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DefaultBaseIT extends BaseIT {

    protected final RestTemplate restTemplate = new RestTemplate();
    protected static final ObjectMapper mapper = new ObjectMapper();
    protected String token;

    @SneakyThrows
    protected <R> R postRest(String url, Object req, Class<R> clazz) {
        HttpHeaders headers = new HttpHeaders();
        if (token != null && !token.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, token);
        }
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(req), headers);
        ResponseEntity<R> response = restTemplate.postForEntity(url, request, clazz);
        return response.getBody();
    }
}
