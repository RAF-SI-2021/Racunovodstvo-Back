package raf.si.racunovodstvo.knjizenje.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.model.Povracaj;
import raf.si.racunovodstvo.knjizenje.services.PovracajService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PovracajiControllerTest {
    private static final Long MOCK_ID = 1L;

    @InjectMocks
    private PovracajiController povracajiController;
    @Mock
    private PovracajService povracajService;

    @Test
    void findAll() {
        Page<Povracaj> pages = Mockito.mock(Page.class);
        given(povracajService.findAll(any(Pageable.class))).willReturn(pages);
        ResponseEntity<?> responseEntity = povracajiController.getPovracaji(0, 50, new String[] {"povracajId"});
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void createPovracaj() {
        ResponseEntity<?> responseEntity = povracajiController.createPovracaj(new Povracaj());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updatePovracaj() {
        Povracaj povracaj = new Povracaj();
        povracaj.setPovracajId(MOCK_ID);
        given(povracajService.findById(MOCK_ID)).willReturn(Optional.of(povracaj));
        ResponseEntity<?> responseEntity = povracajiController.updatePovracaj(povracaj);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updatePovracajException() {
        Povracaj povracaj = new Povracaj();
        povracaj.setPovracajId(MOCK_ID);
        given(povracajService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> povracajiController.updatePovracaj(povracaj));
    }

    @Test
    void deletePovracaj() {
        Povracaj povracaj = new Povracaj();
        povracaj.setPovracajId(MOCK_ID);
        given(povracajService.findById(MOCK_ID)).willReturn(Optional.of(povracaj));
        ResponseEntity<?> responseEntity = povracajiController.deletePovracaj(MOCK_ID);

        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deletePovracajException() {
        given(povracajService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> povracajiController.deletePovracaj(MOCK_ID));
    }
}
