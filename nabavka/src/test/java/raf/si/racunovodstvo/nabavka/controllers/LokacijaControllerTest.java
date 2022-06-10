package raf.si.racunovodstvo.nabavka.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.nabavka.model.Lokacija;
import raf.si.racunovodstvo.nabavka.services.impl.LokacijaService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LokacijaControllerTest {

    @InjectMocks
    private LokacijaController lokacijaController;
    @Mock
    private LokacijaService lokacijaService;

    private static final Long MOCK_ID = 1L;

    private Lokacija getLokacija(){
        Lokacija lokacija = new Lokacija();
        lokacija.setLokacijaId(MOCK_ID);
        lokacija.setNaziv("Naziv");
        lokacija.setAdresa("Adresa");
        return lokacija;
    }

    @Test
    void createLokacija() {
        ResponseEntity<?> responseEntity = lokacijaController.createLokacija(getLokacija());

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateLokacija() {
        ResponseEntity<?> responseEntity = lokacijaController.updateLokacija(getLokacija());

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getLokacije() {
        ResponseEntity<?> responseEntity = lokacijaController.getLokacije();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteLokacija() {
        ResponseEntity<?> responseEntity = lokacijaController.deleteLokacija(MOCK_ID);
        assertEquals(204, responseEntity.getStatusCodeValue());
    }
}
