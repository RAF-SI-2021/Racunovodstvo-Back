package raf.si.racunovodstvo.preduzece.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.preduzece.model.Koeficijent;
import raf.si.racunovodstvo.preduzece.services.impl.KoeficijentService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KoeficijentControllerTest {

    @InjectMocks
    private KoeficijentController koeficijentController;
    @Mock
    private KoeficijentService koeficijentService;

    private static final Long MOCK_ID = 1L;


    @Test
    void findAll() {
        ResponseEntity<?> responseEntity = koeficijentController.findAll();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void update() {
        Koeficijent koeficijent = new Koeficijent();
        koeficijent.setKoeficijentId(MOCK_ID);
        given(koeficijentService.findById(koeficijent.getKoeficijentId())).willReturn(Optional.of(koeficijent));

        ResponseEntity<?> responseEntity = koeficijentController.update(koeficijent);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateException() {
        Koeficijent koeficijent = new Koeficijent();
        koeficijent.setKoeficijentId(MOCK_ID);
        given(koeficijentService.findById(koeficijent.getKoeficijentId())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> koeficijentController.update(koeficijent));
    }

    @Test
    void create() {
        Koeficijent koeficijent = new Koeficijent();
        ResponseEntity<?> responseEntity = koeficijentController.create(koeficijent);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
