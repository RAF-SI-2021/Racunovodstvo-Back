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
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;
import raf.si.racunovodstvo.knjizenje.repositories.FakturaRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FakturaIntegrationTest extends DefaultBaseIT {

    private final static String URI = "/api/faktura";

    private Long fakturaId;

    @Autowired
    private FakturaRepository fakturaRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private static final String MOCK_BR_FAKTURE = "MOCK_BR_FAKTURE";
    private static final Double MOCK_PRODAJNA_VREDNOST = 123.45;
    private static final Double MOCK_RABAT_PROCENAT = 11.00;
    private static final Double MOCK_POREZ_PROCENAT = 33.00;
    private static final String MOCK_VALUTA = "MOCK_VALUTA";
    private static final Double MOCK_KURS = 117.50;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        LoginRequest loginRequest = new LoginRequest("user1", "user1");
        String loginUrl = "http://" + userContainer.getHost() + ":" + userContainer.getMappedPort(8086) + "/auth/login";
        LoginResponse loginResponse = postRest(loginUrl, loginRequest, LoginResponse.class);
        token = "Bearer " + loginResponse.getJwt();

        Faktura fu1 = new Faktura();
        fu1.setBrojFakture(MOCK_BR_FAKTURE);
        fu1.setBrojDokumenta(fu1.getBrojFakture());
        fu1.setDatumIzdavanja(new Date());
        fu1.setRokZaPlacanje(new Date());
        fu1.setDatumPlacanja(new Date());
        fu1.setProdajnaVrednost(MOCK_PRODAJNA_VREDNOST);
        fu1.setRabatProcenat(MOCK_RABAT_PROCENAT);
        fu1.setPorezProcenat(MOCK_POREZ_PROCENAT);
        fu1.setValuta(MOCK_VALUTA);
        fu1.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fu1.setTipDokumenta(TipDokumenta.FAKTURA);
        fu1.setKurs(MOCK_KURS);
        fu1.setNaplata(0.00);
        fu1 = fakturaRepository.save(fu1);
        fakturaId = fu1.getDokumentId();
    }

    @Test
    @Order(1)
    void findAllTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URI + "/all").header("Authorization", token)).andExpect(status().isOk()).andReturn();
        mvcResult.getResponse().getContentAsString();
        List<Faktura> responseList = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertNotNull(responseList);
        assertTrue(responseList.stream().anyMatch(faktura -> faktura.getDokumentId().equals(fakturaId)));
    }

    @Test
    @Order(2)
    void deleteByIdTest() throws Exception {
        mockMvc.perform(delete(URI + "/" + fakturaId).header("Authorization", token)).andExpect(status().isNoContent());
        assertTrue(fakturaRepository.findById(fakturaId).isEmpty());
    }
}
