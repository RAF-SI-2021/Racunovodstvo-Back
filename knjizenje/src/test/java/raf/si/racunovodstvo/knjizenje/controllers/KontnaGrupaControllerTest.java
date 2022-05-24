package raf.si.racunovodstvo.knjizenje.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;
import raf.si.racunovodstvo.knjizenje.services.KontnaGrupaService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KontnaGrupaControllerTest {

    @InjectMocks
    private KontnaGrupaController kontnaGrupaController;
    @Mock
    private KontnaGrupaService kontnaGrupaService;

    private static final Long MOCK_ID = 1L;

    @Test
    void getKontnaGrupa() {
        KontnaGrupa kontnaGrupa = new KontnaGrupa();
        given(kontnaGrupaService.findById(MOCK_ID)).willReturn(Optional.of(kontnaGrupa));

        ResponseEntity<?> responseEntity = kontnaGrupaController.getKontnaGrupa(MOCK_ID);
        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }

    @Test
    void getKontnaGrupaException() {
        given(kontnaGrupaService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> kontnaGrupaController.getKontnaGrupa(MOCK_ID));
    }

    @Test
    void getKontneGrupe() {
        Integer page = 1;
        Integer size = 50;
        String[] sort = new String[1];
        sort[0] = "1";

        ResponseEntity<?> responseEntity = kontnaGrupaController.getKontneGrupe(page, size, sort);
        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }

    @Test
    void createKontnaGrupa() {
        ResponseEntity<?> responseEntity = kontnaGrupaController.createKontnaGrupa(new KontnaGrupa());
        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }

    @Test
    void updateKontnaGrupa() {
        given(kontnaGrupaService.findById(MOCK_ID)).willReturn(Optional.of(new KontnaGrupa()));
        ResponseEntity<?> responseEntity = kontnaGrupaController.updateKontnaGrupa(new KontnaGrupa(), MOCK_ID);
        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }

    @Test
    void updateKontnaGrupaException() {
        given(kontnaGrupaService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> kontnaGrupaController.updateKontnaGrupa(new KontnaGrupa(), MOCK_ID));
    }

    @Test
    void deleteKontnaGrupa() {
        given(kontnaGrupaService.findById(MOCK_ID)).willReturn(Optional.of(new KontnaGrupa()));
        ResponseEntity<?> responseEntity = kontnaGrupaController.deleteKontnaGrupa(MOCK_ID);
        assertEquals(responseEntity.getStatusCodeValue(), 204);
    }

    @Test
    void deleteKontnaGrupaException() {
        given(kontnaGrupaService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> kontnaGrupaController.deleteKontnaGrupa(MOCK_ID));
    }
}