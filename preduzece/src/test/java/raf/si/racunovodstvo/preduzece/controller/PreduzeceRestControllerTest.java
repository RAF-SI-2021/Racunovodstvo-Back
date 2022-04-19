package raf.si.racunovodstvo.preduzece.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.preduzece.model.Preduzece;
import raf.si.racunovodstvo.preduzece.services.impl.PreduzeceService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PreduzeceRestControllerTest {

    @InjectMocks
    private PreduzeceRestController preduzeceRestController;
    @Mock
    private PreduzeceService preduzeceService;

    private static final Long MOCK_ID = 1L;


    @Test
    void getAllPreduzece() {
        ResponseEntity<?> responseEntity = preduzeceRestController.getAllPreduzece();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getPreduzeceById() {
        Preduzece preduzece = new Preduzece();
        given(preduzeceService.findById(MOCK_ID)).willReturn(Optional.of(preduzece));
        ResponseEntity<?> responseEntity = preduzeceRestController.getPreduzeceById(MOCK_ID);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getPreduzeceByIdException() {
        given(preduzeceService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> preduzeceRestController.getPreduzeceById(MOCK_ID));
    }

    @Test
    void createPreduzece() {
        Preduzece preduzece = new Preduzece();
        ResponseEntity<?> responseEntity = preduzeceRestController.createPreduzece(preduzece);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updatePreduzece() {
        Preduzece preduzece = new Preduzece();
        preduzece.setPreduzeceId(MOCK_ID);
        given(preduzeceService.findById(MOCK_ID)).willReturn(Optional.of(preduzece));

        ResponseEntity<?> responseEntity = preduzeceRestController.updatePreduzece(preduzece);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updatePreduzeceException() {
        Preduzece preduzece = new Preduzece();
        preduzece.setPreduzeceId(MOCK_ID);
        given(preduzeceService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> preduzeceRestController.updatePreduzece(preduzece));
    }

    @Test
    void deletePreduzece() {
        Preduzece preduzece = new Preduzece();
        given(preduzeceService.findById(MOCK_ID)).willReturn(Optional.of(preduzece));

        ResponseEntity<?> responseEntity = preduzeceRestController.deletePreduzece(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void deletePreduzeceException() {
        given(preduzeceService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> preduzeceRestController.deletePreduzece(MOCK_ID));
    }
}