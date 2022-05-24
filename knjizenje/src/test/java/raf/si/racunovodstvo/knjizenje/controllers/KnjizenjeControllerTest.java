package raf.si.racunovodstvo.knjizenje.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.services.KnjizenjeService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KnjizenjeControllerTest {

    @InjectMocks
    private KnjizenjeController knjizenjeController;
    @Mock
    private KnjizenjeService knjizenjeService;

    private static final Long MOCK_ID = 1L;


    @Test
    void createDnevnikKnjizenja() {
        ResponseEntity<?> responseEntity = knjizenjeController.createDnevnikKnjizenja(new Knjizenje());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateDnevnikKnjizenja() {
        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(MOCK_ID);
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.of(knjizenje));
        ResponseEntity<?> responseEntity = knjizenjeController.updateDnevnikKnjizenja(knjizenje);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateDnevnikKnjizenjaException() {
        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(MOCK_ID);
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> knjizenjeController.updateDnevnikKnjizenja(knjizenje));
    }

    @Test
    void deleteDnevnikKnjizenja() {
        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(MOCK_ID);
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.of(knjizenje));
        ResponseEntity<?> responseEntity = knjizenjeController.deleteDnevnikKnjizenja(MOCK_ID);

        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteDnevnikKnjizenjaException() {
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> knjizenjeController.deleteDnevnikKnjizenja(MOCK_ID));
    }

    @Test
    void getDnevnikKnjizenjaId() {
        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(MOCK_ID);
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.of(knjizenje));
        ResponseEntity<?> responseEntity = knjizenjeController.getDnevnikKnjizenjaId(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getDnevnikKnjizenjaIdException() {
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> knjizenjeController.getDnevnikKnjizenjaId(MOCK_ID));
    }

    @Test
    void search() {
        String search = "knjizenjeId:1";
        Integer page = 1;
        Integer size = 50;
        String[] sort = new String[1];
        sort[0] = "1";

        ResponseEntity<?> responseEntity = knjizenjeController.search(search, page, size, sort);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void searchException() {
        String search = "abc";
        Integer page = 1;
        Integer size = 50;
        String[] sort = new String[1];
        sort[0] = "1";

        assertThrows(OperationNotSupportedException.class, () -> knjizenjeController.search(search, page, size, sort));
    }

    @Test
    void findAll() {
        ResponseEntity<?> responseEntity = knjizenjeController.findAll();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}