package raf.si.racunovodstvo.nabavka.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import raf.si.racunovodstvo.nabavka.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.nabavka.feign.UserFeignClient;
import raf.si.racunovodstvo.nabavka.integration.test_model.LoginRequest;
import raf.si.racunovodstvo.nabavka.integration.test_model.LoginResponse;
import raf.si.racunovodstvo.nabavka.integration.test_model.PreduzeceRequest;
import raf.si.racunovodstvo.nabavka.requests.KonverzijaRequest;
import raf.si.racunovodstvo.nabavka.requests.LokacijaRequest;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;
import raf.si.racunovodstvo.nabavka.responses.PreduzeceResponse;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExternalServiceIntegrationTest extends BaseIT {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private String token;

    private final static String URI_KONVERZIJE = "/api/konverzije";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PreduzeceFeignClient preduzeceFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    private MockMvc mockMvc;

    @BeforeAll
    void setup() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        LoginRequest loginRequest = new LoginRequest("user1", "user1");
        String loginUrl = "http://" + userContainer.getHost() + ":" + userContainer.getMappedPort(8086) + "/auth/login";
        LoginResponse loginResponse = postRest(loginUrl, loginRequest, LoginResponse.class);
        token = "Bearer " + loginResponse.getJwt();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
    }

    @Test
    void createKonverzijaTest() throws Exception {
        KonverzijaRequest konverzijaRequest = new KonverzijaRequest();
        LokacijaRequest lokacijaRequest = new LokacijaRequest();
        lokacijaRequest.setAdresa("testAdresa");
        lokacijaRequest.setNaziv("testNaziv");
        konverzijaRequest.setLokacija(lokacijaRequest);
        konverzijaRequest.setBrojKonverzije("1234TEST");
        konverzijaRequest.setValuta("EUR");
        konverzijaRequest.setDatum(new Date());
        konverzijaRequest.setDobavljacId(1L);
        konverzijaRequest.setFakturnaCena(1000.0);
        konverzijaRequest.setNabavnaVrednost(1000.0);
        konverzijaRequest.setTroskoviNabavke(new ArrayList<>());
        String request = mapper.writeValueAsString(konverzijaRequest);
        String result = mockMvc.perform(post(URI_KONVERZIJE).header(HttpHeaders.AUTHORIZATION, token)
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .content(request))
                               .andExpect(status().isOk())
                               .andReturn()
                               .getResponse()
                               .getContentAsString();
        KonverzijaResponse response = mapper.readValue(result, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(konverzijaRequest.getBrojKonverzije(), response.getBrojKonverzije());
    }

    @Test
    void testPreduzeceFeign() {
        PreduzeceResponse response = preduzeceFeignClient.getPreduzeceById(1L, token).getBody();

        assertNotNull(response);
        assertEquals(1L, response.getPreduzeceId());
    }

    @Test
    void testAccess() {
        int status = userFeignClient.validateToken(token).getStatusCodeValue();

        assertEquals(200, status);
    }

    @Test
    void testAccessFail() {
        assertThrows(FeignException.class, () -> userFeignClient.validateToken("WRONG" + token));
    }

    @SneakyThrows
    private <R> R postRest(String url, Object req, Class<R> clazz) {
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
