package raf.si.racunovodstvo.knjizenje.integration;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import raf.si.racunovodstvo.knjizenje.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.knjizenje.feign.UserFeignClient;
import raf.si.racunovodstvo.knjizenje.integration.test_model.LoginRequest;
import raf.si.racunovodstvo.knjizenje.integration.test_model.LoginResponse;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;
import raf.si.racunovodstvo.knjizenje.repositories.FakturaRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExternalServiceIntegrationTest extends DefaultBaseIT {

    private String token;

    private final static String URI_IZVESTAJI = "/api/izvestaji";
    private final static String URI_FAKTURA = "/api/faktura";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FakturaRepository fakturaRepository;

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
    void getBilansStanjaTest() throws Exception {
        Long preduzece = 1L;
        String title = "title";
        String datumOd = "2022-06-01";
        String datumDo = "2022-06-30";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("preduzece", preduzece + "");
        params.add("title", title);
        params.add("datumiOd", datumOd);
        params.add("datumiDo", datumDo);
        mockMvc.perform(get(URI_IZVESTAJI + "/stanje").params(params).header("Authorization", token))
               .andExpect(status().isOk());
    }

    @Test
    void createFakturaTest() throws Exception {
        Faktura faktura = new Faktura();
        faktura.setPreduzeceId(1L);
        faktura.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        faktura.setRokZaPlacanje(new Date());
        faktura.setBrojFakture("12345TEST");
        faktura.setValuta("EUR");
        faktura.setPorezProcenat(10.0);
        faktura.setNaplata(1000.00);
        faktura.setIznos(1100.00);
        faktura.setKurs(117.00);
        faktura.setProdajnaVrednost(1000.0);
        faktura.setRabatProcenat(10.0);
        faktura.setRabat(1000.0);
        faktura.setPorez(1000.0);
        faktura.setTipDokumenta(TipDokumenta.FAKTURA);
        faktura.setDatumPlacanja(new Date());
        faktura.setDatumIzdavanja(new Date());
        faktura.setBrojDokumenta("12345TEST");
        String requestJson = mapper.writeValueAsString(faktura);
        String result = mockMvc.perform(post(URI_FAKTURA).contentType(MediaType.APPLICATION_JSON)
                                                         .content(requestJson)
                                                         .header("Authorization", token))
                               .andExpect(status().isOk())
                               .andReturn()
                               .getResponse()
                               .getContentAsString();
        Faktura createdFaktura = mapper.readValue(result, new TypeReference<>() {
        });
        Optional<Faktura> optionalFaktura = fakturaRepository.findByDokumentId(createdFaktura.getDokumentId());
        assertTrue(optionalFaktura.isPresent());
    }

    @Test
    void testPreduzeceFeign() {
        Preduzece response = preduzeceFeignClient.getPreduzeceById(1L, token).getBody();

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
}
