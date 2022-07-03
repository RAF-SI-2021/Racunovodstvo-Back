package raf.si.racunovodstvo.knjizenje.integration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import raf.si.racunovodstvo.knjizenje.integration.test_model.LoginRequest;
import raf.si.racunovodstvo.knjizenje.integration.test_model.LoginResponse;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.TransakcijaRepository;

import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransakcijaIntegrationTest extends DefaultBaseIT {

    private final static String URI = "/api/transakcije";

    private Long transakcijaId;

    @Autowired
    private TransakcijaRepository transakcijaRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private static final TipTransakcije MOCK_TIP_TRANSAKCIJE = TipTransakcije.ISPLATA;
    private static final String MOCK_BROJ_TRANSAKCIJE = "123";
    private static final double MOCK_IZNOS = 22.3;
    private static final Long MOCK_ID = 1L;
    private static final String MOCK_BROJ_DOKUMENTA = "1111";
    private static final TipDokumenta MOCK_TIP_DOKUMENTA = TipDokumenta.TRANSAKCIJA;
    private static final Date MOCK_DATUM = new Date();

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        LoginRequest loginRequest = new LoginRequest("user1", "user1");
        String loginUrl = "http://" + userContainer.getHost() + ":" + userContainer.getMappedPort(8086) + "/auth/login";
        LoginResponse loginResponse = postRest(loginUrl, loginRequest, LoginResponse.class);
        token = "Bearer " + loginResponse.getJwt();

        Transakcija tr = new Transakcija();
        tr.setTipTransakcije(MOCK_TIP_TRANSAKCIJE);
        tr.setIznos(MOCK_IZNOS);
        tr.setBrojTransakcije(MOCK_BROJ_TRANSAKCIJE);
        tr.setDokumentId(MOCK_ID);
        tr.setBrojDokumenta(MOCK_BROJ_DOKUMENTA);
        tr.setTipDokumenta(MOCK_TIP_DOKUMENTA);
        tr.setDatumTransakcije(MOCK_DATUM);
        tr = transakcijaRepository.save(tr);
        transakcijaId = tr.getDokumentId();
    }

    @Test
    @Order(1)
    void deleteUnauthorizedTest() throws Exception {
        mockMvc.perform(delete(URI + "/" + transakcijaId).header("Authorization", "WRONG")).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(1)
    void deleteTest() throws Exception {
        mockMvc.perform(delete(URI + "/" + transakcijaId).header("Authorization", token)).andExpect(status().isNoContent());
    }

    @Test
    @Order(2)
    void deleteNotFoundTest() throws Exception {
        mockMvc.perform(delete(URI + "/" + transakcijaId).header("Authorization", token)).andExpect(status().isNotFound());
    }
}
