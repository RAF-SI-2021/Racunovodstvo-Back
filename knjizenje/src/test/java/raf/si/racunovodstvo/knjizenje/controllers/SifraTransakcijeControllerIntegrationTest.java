package raf.si.racunovodstvo.knjizenje.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.SifraTransakcijeRepository;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SifraTransakcijeControllerIntegrationTest {

    private final static String URI = "/api/sifraTransakcije";

    @Autowired
    private SifraTransakcijeRepository sifraTransakcijeRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void findAllTest2() throws Exception {
        SifraTransakcije st = new SifraTransakcije();

        st.setSifra(111L);
        st.setNazivTransakcije("stojedanaest");
        st.setSifraTransakcijeId(1L);

        sifraTransakcijeRepository.save(st);

        MvcResult mvcResult = mockMvc.perform(get(URI).header("Authorization", "sssss")).andExpect(status().isOk()).andReturn();
        mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

        List<SifraTransakcijeResponse> lista = mapper.convertValue(map.get("content"), new TypeReference<>() {});

        assertTrue(lista.stream().anyMatch(sifraTransakcijeResponse -> sifraTransakcijeResponse.getSifra().equals(st.getSifra())));

    }
}
