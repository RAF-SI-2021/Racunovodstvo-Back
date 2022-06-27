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
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.requests.BazniCentarRequest;
import raf.si.racunovodstvo.knjizenje.requests.ProfitniCentarRequest;
import raf.si.racunovodstvo.knjizenje.responses.ProfitniCentarResponse;
import raf.si.racunovodstvo.knjizenje.services.ProfitniCentarService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProfitniCentarControllerTest {
    private static final Long MOCK_ID = 1L;

    @InjectMocks
    private ProfitniCentarController profitniCentarController;
    @Mock
    private ProfitniCentarService profitniCentarService;

    @Test
    void findAll() {
        List<ProfitniCentarResponse> responses = Mockito.mock(List.class);
        given(profitniCentarService.findAllProfitniCentarResponse()).willReturn(responses);
        ResponseEntity<?> responseEntity = profitniCentarController.findAll();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void findById() {
        ProfitniCentar profitniCentar = new ProfitniCentar();
        profitniCentar.setId(MOCK_ID);
        given(profitniCentarService.findById(MOCK_ID)).willReturn(Optional.of(profitniCentar));
        ResponseEntity<?> responseEntity = profitniCentarController.getProfitniCentarById(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void findByIdNotFound() {
        given(profitniCentarService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> profitniCentarController.getProfitniCentarById(MOCK_ID));
    }

    @Test
    void createProfitniCentar() {
        ResponseEntity<?> responseEntity = profitniCentarController.createProfitniCentar(new ProfitniCentar());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateProfitniCentar() {
        ProfitniCentar profitniCentar = new ProfitniCentar();
        profitniCentar.setId(MOCK_ID);
        ProfitniCentarRequest request = new ProfitniCentarRequest();
        request.setId(MOCK_ID);
        given(profitniCentarService.findById(MOCK_ID)).willReturn(Optional.of(profitniCentar));
        ResponseEntity<?> responseEntity = profitniCentarController.updateProfitniCentar(request);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateProfitniCentarException() {
        ProfitniCentar profitniCentar = new ProfitniCentar();
        profitniCentar.setId(MOCK_ID);
        ProfitniCentarRequest request = new ProfitniCentarRequest();
        request.setId(MOCK_ID);
        given(profitniCentarService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> profitniCentarController.updateProfitniCentar(request));
    }

    @Test
    void deleteProfitniCentar() {
        ProfitniCentar profitniCentar = new ProfitniCentar();
        profitniCentar.setId(MOCK_ID);
        given(profitniCentarService.findById(MOCK_ID)).willReturn(Optional.of(profitniCentar));
        ResponseEntity<?> responseEntity = profitniCentarController.deleteProfitniCentar(MOCK_ID);

        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteProfitniCentarException() {
        given(profitniCentarService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> profitniCentarController.deleteProfitniCentar(MOCK_ID));
    }

    @Test
    void findAllPageable() {
        Page<ProfitniCentar> pages = Mockito.mock(Page.class);
        given(profitniCentarService.findAll(any(Pageable.class))).willReturn(pages);
        ResponseEntity<?> responseEntity = profitniCentarController.findAll(0, 50, new String[] {"sort"});
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(pages, responseEntity.getBody());
    }

    @Test
    void addKontosFromKnjizenje() {
        ProfitniCentar expected = Mockito.mock(ProfitniCentar.class);
        BazniCentarRequest request = Mockito.mock(BazniCentarRequest.class);
        given(profitniCentarService.findById(request.getBazniCentarId())).willReturn(Optional.of(expected));
        Knjizenje k = new Knjizenje();
        given(request.getKnjizenje()).willReturn(k);
        given(profitniCentarService.addKontosFromKnjizenje(k, expected)).willReturn(expected);
        ResponseEntity<?> responseEntity = profitniCentarController.addKontosFromKnjizenje(request);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    void addKontosFromKnjizenjeNotFound() {
        BazniCentarRequest request = Mockito.mock(BazniCentarRequest.class);
        given(profitniCentarService.findById(request.getBazniCentarId())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> profitniCentarController.addKontosFromKnjizenje(request));
    }

    @Test
    void deleteKontoFromProfitniCentar() {
        BazniKonto expected = Mockito.mock(BazniKonto.class);
        given(profitniCentarService.findBazniKontoById(MOCK_ID)).willReturn(Optional.of(expected));
        ResponseEntity<?> responseEntity = profitniCentarController.deleteKontoFromProfitniCentar(MOCK_ID);
        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteKontoFromProfitniCentarNotFound() {
        given(profitniCentarService.findBazniKontoById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> profitniCentarController.deleteKontoFromProfitniCentar(MOCK_ID));
    }
}
