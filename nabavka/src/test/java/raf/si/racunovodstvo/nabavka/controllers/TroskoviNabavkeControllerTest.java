package raf.si.racunovodstvo.nabavka.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.services.impl.TroskoviNabavkeService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TroskoviNabavkeControllerTest {

    @InjectMocks
    private TroskoviNabavkeController troskoviNabavkeController;
    @Mock
    private TroskoviNabavkeService troskoviNabavkeService;

    private static final Long MOCK_ID = 1L;

    private TroskoviNabavke getTrosakNabavke(){
        TroskoviNabavke troskoviNabavke = new TroskoviNabavke();
        troskoviNabavke.setTroskoviNabavkeId(MOCK_ID);
        troskoviNabavke.setNaziv("Naziv");
        troskoviNabavke.setCena(100.0);
        return troskoviNabavke;
    }

    @Test
    void createTroskoviNabavke() {
        ResponseEntity<?> responseEntity = troskoviNabavkeController.createTroskoviNabavke(getTrosakNabavke());

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateTroskoviNabavke() {
        ResponseEntity<?> responseEntity = troskoviNabavkeController.updateTroskoviNabavke(getTrosakNabavke());

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getTroskoviNabavke() {
        ResponseEntity<?> responseEntity = troskoviNabavkeController.getTroskoviNabavke();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteTroskoviNabavke() {
        ResponseEntity<?> responseEntity = troskoviNabavkeController.deleteTroskoviNabavke(MOCK_ID);
        assertEquals(204, responseEntity.getStatusCodeValue());
    }
}
