package raf.si.racunovodstvo.knjizenje.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import raf.si.racunovodstvo.knjizenje.integration.test_model.LoginRequest;
import raf.si.racunovodstvo.knjizenje.integration.test_model.LoginResponse;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.SifraTransakcijeRepository;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SifraTransakcijeIntegrationTest extends DefaultBaseIT {

    private final static String URI = "/api/sifraTransakcije";
    private Long sifra;
    private Long id;

    @Autowired
    private SifraTransakcijeRepository sifraTransakcijeRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        LoginRequest loginRequest = new LoginRequest("user1", "user1");
        String loginUrl = "http://" + userContainer.getHost() + ":" + userContainer.getMappedPort(8086) + "/auth/login";
        LoginResponse loginResponse = postRest(loginUrl, loginRequest, LoginResponse.class);
        token = "Bearer " + loginResponse.getJwt();

        SifraTransakcije st = new SifraTransakcije();

        st.setSifra(12321L);
        st.setNazivTransakcije("stojedanaest");
        st = sifraTransakcijeRepository.save(st);
        sifra = st.getSifra();
        id = st.getSifraTransakcijeId();
    }

    @Test
    @Order(1)
    void findAllTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URI).header("Authorization", token)).andExpect(status().isOk()).andReturn();
        Map<String, Object> map = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        List<SifraTransakcijeResponse> lista = mapper.convertValue(map.get("content"), new TypeReference<>() {
        });

        assertTrue(lista.stream().anyMatch(sifraTransakcijeResponse -> sifraTransakcijeResponse.getSifra().equals(sifra)));
    }

    @Test
    @Order(1)
    void findByIdTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URI + "/" + id).header("Authorization", token)).andExpect(status().isOk()).andReturn();
        SifraTransakcije response = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(sifra, response.getSifra());
    }

    @Test
    @Order(2)
    void deleteTest() throws Exception {
        mockMvc.perform(delete(URI + "/" + id).header("Authorization", token)).andExpect(status().isNoContent()).andReturn();
        assertTrue(sifraTransakcijeRepository.findBySifra(sifra).isEmpty());
    }

    @Test
    @Order(3)
    void findByIdNotFoundTest() throws Exception {
        mockMvc.perform(get(URI + "/" + id).header("Authorization", token)).andExpect(status().isNotFound());
    }
}
