package raf.si.racunovodstvo.knjizenje.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.*;
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
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.repositories.ProfitniCentarRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfitniCentarIntegrationTest extends DefaultBaseIT {

    private final static String URI = "/api/profitni-centri";

    private Long centarId;

    @Autowired
    private ProfitniCentarRepository profitniCentarRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private static final String MOCK_SIFRA = "MOCK_SIFRA";
    private static final String MOCK_NAZIV = "MOCK_NAZIV";
    private static final Long MOCK_LOKACIJAID = 11L;
    private static final Long MOCK_LICEID = 1L;
    private static final Double MOCK_TROSAK = 200.00;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        LoginRequest loginRequest = new LoginRequest("user1", "user1");
        String loginUrl = "http://" + userContainer.getHost() + ":" + userContainer.getMappedPort(8086) + "/auth/login";
        LoginResponse loginResponse = postRest(loginUrl, loginRequest, LoginResponse.class);
        token = "Bearer " + loginResponse.getJwt();

        ProfitniCentar pc1 = new ProfitniCentar();
        pc1.setSifra(MOCK_SIFRA);
        pc1.setNaziv(MOCK_NAZIV);
        pc1.setLokacijaId(MOCK_LOKACIJAID);
        pc1.setOdgovornoLiceId(MOCK_LICEID);
        pc1.setUkupniTrosak(MOCK_TROSAK);
        pc1 = profitniCentarRepository.save(pc1);
        centarId = pc1.getId();
    }

    @Test
    @Order(1)
    void findByIdUnauthorizedTest() throws Exception {
        MvcResult mvcResult =
            mockMvc.perform(get(URI + "/" + centarId).header("Authorization", token + "WRONG")).andExpect(status().isOk()).andReturn();
        ProfitniCentar dva = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(MOCK_SIFRA, dva.getSifra());
    }

    @Test
    @Order(1)
    void findByIdTest() throws Exception {
        MvcResult mvcResult =
            mockMvc.perform(get(URI + "/" + centarId).header("Authorization", token)).andExpect(status().isOk()).andReturn();
        ProfitniCentar dva = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(MOCK_SIFRA, dva.getSifra());
    }
}
