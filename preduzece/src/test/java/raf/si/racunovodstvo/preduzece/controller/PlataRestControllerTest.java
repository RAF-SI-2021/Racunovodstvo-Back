package raf.si.racunovodstvo.preduzece.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.preduzece.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.preduzece.model.Plata;
import raf.si.racunovodstvo.preduzece.requests.PlataRequest;
import raf.si.racunovodstvo.preduzece.services.impl.PlataService;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PlataRestControllerTest {

    @InjectMocks
    private PlataRestController plataRestController;
    @Mock
    private PlataService plataService;

    private static final Long MOCK_ID = 1L;


    @Test
    void getPlataForZaposleni() {
        ResponseEntity<?> responseEntity = plataRestController.getPlataForZaposleni(MOCK_ID);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getPlata() {
        ResponseEntity<?> responseEntity = plataRestController.getPlata("porez>0");
        assertEquals(200, responseEntity.getStatusCodeValue());    }

    @Test
    void getPlataException() {
        assertThrows(OperationNotSupportedException.class, () -> plataRestController.getPlata("abc"));
    }

    @Test
    void getAllPlata() {
        ResponseEntity<?> responseEntity = plataRestController.getAllPlata();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getPlataById() {
        Plata plata = new Plata();
        given(plataService.findById(MOCK_ID)).willReturn(Optional.of(plata));
        ResponseEntity<?> responseEntity = plataRestController.getPlataById(MOCK_ID);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getPlataByIdException() {
        given(plataService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> plataRestController.getPlataById(MOCK_ID));
    }

    @Test
    void newPlata() {
        PlataRequest plataRequest = new PlataRequest(MOCK_ID, 500.0, new Date(), MOCK_ID);
        ResponseEntity<?> responseEntity = plataRestController.newPlata(plataRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void newPlataException() {
        PlataRequest plataRequest = new PlataRequest(MOCK_ID, 500.0, new Date(), MOCK_ID);
        given(plataService.save(plataRequest)).willThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> plataRestController.newPlata(plataRequest));
    }

    @Test
    void editPlata() {
        Plata plata = new Plata();
        PlataRequest plataRequest = new PlataRequest(MOCK_ID, 500.0, new Date(), MOCK_ID);
        given(plataService.findById(MOCK_ID)).willReturn(Optional.of(plata));

        ResponseEntity<?> responseEntity = plataRestController.editPlata(plataRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void editPlataException1() {
        Plata plata = new Plata();
        PlataRequest plataRequest = new PlataRequest(MOCK_ID, 500.0, new Date(), MOCK_ID);
        given(plataService.findById(MOCK_ID)).willReturn(Optional.of(plata));
        given(plataService.save(plataRequest)).willThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> plataRestController.editPlata(plataRequest));
    }

    @Test
    void editPlataException2() {
        PlataRequest plataRequest = new PlataRequest(MOCK_ID, 500.0, new Date(), MOCK_ID);
        given(plataService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> plataRestController.editPlata(plataRequest));
    }

    @Test
    void deletePlata() {
        Plata plata = new Plata();
        given(plataService.findById(MOCK_ID)).willReturn(Optional.of(plata));

        ResponseEntity<?> responseEntity = plataRestController.deletePlata(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void deletePlataException() {
        given(plataService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> plataRestController.deletePlata(MOCK_ID));
    }
}