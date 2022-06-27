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
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.requests.SifraTransakcijeRequest;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.knjizenje.services.SifraTransakcijeService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SifraTransakcijeControllerTest {
    private static final Long MOCK_ID = 1L;
    private static final String MOCK_TOCKEN = "MOCK_TOKEN";

    @InjectMocks
    private SifraTransakcijeController sifraTransakcijeController;
    @Mock
    private SifraTransakcijeService sifraTransakcijeService;

    @Test
    void findAllPageable() {
        Page<SifraTransakcijeResponse> pages = Mockito.mock(Page.class);
        given(sifraTransakcijeService.findAll(any(Pageable.class), eq(MOCK_TOCKEN))).willReturn(pages);
        ResponseEntity<?> responseEntity = sifraTransakcijeController.findAll("", 0, 50, new String[] {"sort"}, MOCK_TOCKEN);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(pages, responseEntity.getBody());
    }

    @Test
    void findById() {
        SifraTransakcije sifraTransakcije = new SifraTransakcije();
        sifraTransakcije.setSifraTransakcijeId(MOCK_ID);
        given(sifraTransakcijeService.findById(MOCK_ID)).willReturn(Optional.of(sifraTransakcije));
        ResponseEntity<?> responseEntity = sifraTransakcijeController.getById(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void findByIdNotFound() {
        given(sifraTransakcijeService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sifraTransakcijeController.getById(MOCK_ID));
    }

    @Test
    void createSifraTransakcije() {
        ResponseEntity<?> responseEntity = sifraTransakcijeController.create(new SifraTransakcijeRequest());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateSifraTransakcije() {
        SifraTransakcije sifraTransakcije = new SifraTransakcije();
        sifraTransakcije.setSifraTransakcijeId(MOCK_ID);
        SifraTransakcijeRequest request = new SifraTransakcijeRequest();
        request.setSifraTransakcijeId(MOCK_ID);
        given(sifraTransakcijeService.update(request)).willReturn(new SifraTransakcijeResponse());
        ResponseEntity<?> responseEntity = sifraTransakcijeController.update(request);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteSifraTransakcije() {
        SifraTransakcije sifraTransakcije = new SifraTransakcije();
        sifraTransakcije.setSifraTransakcijeId(MOCK_ID);
        ResponseEntity<?> responseEntity = sifraTransakcijeController.delete(MOCK_ID);

        assertEquals(204, responseEntity.getStatusCodeValue());
    }
}
