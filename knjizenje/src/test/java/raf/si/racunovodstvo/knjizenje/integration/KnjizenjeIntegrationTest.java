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
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KnjizenjeIntegrationTest extends DefaultBaseIT {

    private final static String URI = "/api/knjizenje";

    @Autowired
    private KnjizenjeRepository knjizenjeRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private Long knjizenjeId;

    private static final String MOCK_BR_NALOGA = "MOCK_BROJ_NALOGA";
    private static final Date MOCK_DATUM = new Date();

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        LoginRequest loginRequest = new LoginRequest("user1", "user1");
        String loginUrl = "http://" + userContainer.getHost() + ":" + userContainer.getMappedPort(8086) + "/auth/login";
        LoginResponse loginResponse = postRest(loginUrl, loginRequest, LoginResponse.class);
        token = "Bearer " + loginResponse.getJwt();

        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setBrojNaloga(MOCK_BR_NALOGA);
        knjizenje.setDatumKnjizenja(MOCK_DATUM);
        knjizenje = knjizenjeRepository.save(knjizenje);
        knjizenjeId = knjizenje.getKnjizenjeId();
    }

    @Test
    @Order(1)
    void findByIdTest() throws Exception {
        MvcResult mvcResult =
            mockMvc.perform(get(URI + "/" + knjizenjeId).header("Authorization", token)).andExpect(status().isOk()).andReturn();
        mvcResult.getResponse().getContentAsString();
        Knjizenje dva = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(MOCK_BR_NALOGA, dva.getBrojNaloga());
    }

    @Test
    @Order(1)
    void findByIdUnauthorizedTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URI + "/" + knjizenjeId).header("Authorization", token + "WRONG"))
                                     .andExpect(status().is4xxClientError())
                                     .andReturn();
    }
}
