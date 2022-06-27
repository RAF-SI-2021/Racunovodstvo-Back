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
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;
import raf.si.racunovodstvo.knjizenje.requests.BazniCentarRequest;
import raf.si.racunovodstvo.knjizenje.requests.TroskovniCentarRequest;
import raf.si.racunovodstvo.knjizenje.responses.TroskovniCentarResponse;
import raf.si.racunovodstvo.knjizenje.services.TroskovniCentarService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TroskovniCentarControllerTest {
    private static final Long MOCK_ID = 1L;

    @InjectMocks
    private TroskovniCentarController troskovniCentarController;
    @Mock
    private TroskovniCentarService troskovniCentarService;

    @Test
    void findAllPaged() {
        Page<TroskovniCentar> pages = Mockito.mock(Page.class);
        given(troskovniCentarService.findAll(any(Pageable.class))).willReturn(pages);
        ResponseEntity<?> responseEntity = troskovniCentarController.findAll(0, 50, new String[] {"povracajId"});
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void findAll() {
        List<TroskovniCentarResponse> response = Mockito.mock(List.class);
        given(troskovniCentarService.findAllTroskovniCentriResponse()).willReturn(response);
        ResponseEntity<?> responseEntity = troskovniCentarController.findAll();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void findById() {
        TroskovniCentar troskovniCentar = new TroskovniCentar();
        troskovniCentar.setId(MOCK_ID);
        given(troskovniCentarService.findById(MOCK_ID)).willReturn(Optional.of(troskovniCentar));
        ResponseEntity<?> responseEntity = troskovniCentarController.getTroskovniCentarById(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void findByIdNotFound() {
        given(troskovniCentarService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> troskovniCentarController.getTroskovniCentarById(MOCK_ID));
    }

    @Test
    void createTroskovniCentar() {
        ResponseEntity<?> responseEntity = troskovniCentarController.createTroskovniCentar(new TroskovniCentar());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateTroskovniCentar() {
        TroskovniCentar povracaj = new TroskovniCentar();
        given(troskovniCentarService.findById(MOCK_ID)).willReturn(Optional.of(povracaj));
        TroskovniCentarRequest request = new TroskovniCentarRequest();
        request.setId(MOCK_ID);
        ResponseEntity<?> responseEntity = troskovniCentarController.updateTroskovniCentar(request);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateTroskovniCentarException() {
        given(troskovniCentarService.findById(MOCK_ID)).willReturn(Optional.empty());
        TroskovniCentarRequest request = new TroskovniCentarRequest();
        request.setId(MOCK_ID);
        assertThrows(EntityNotFoundException.class, () -> troskovniCentarController.updateTroskovniCentar(request));
    }

    @Test
    void deleteTroskovniCentar() {
        TroskovniCentar povracaj = new TroskovniCentar();
        povracaj.setId(MOCK_ID);
        given(troskovniCentarService.findById(MOCK_ID)).willReturn(Optional.of(povracaj));
        ResponseEntity<?> responseEntity = troskovniCentarController.deleteTroskovniCentar(MOCK_ID);

        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteTroskovniCentarException() {
        given(troskovniCentarService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> troskovniCentarController.deleteTroskovniCentar(MOCK_ID));
    }

    @Test
    void addKontosFromKnjizenje() {
        TroskovniCentar expected = Mockito.mock(TroskovniCentar.class);
        BazniCentarRequest request = Mockito.mock(BazniCentarRequest.class);
        given(troskovniCentarService.findById(request.getBazniCentarId())).willReturn(Optional.of(expected));
        Knjizenje k = new Knjizenje();
        given(request.getKnjizenje()).willReturn(k);
        given(troskovniCentarService.addKontosFromKnjizenje(k, expected)).willReturn(expected);
        ResponseEntity<?> responseEntity = troskovniCentarController.addKontosFromKnjizenje(request);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    void addKontosFromKnjizenjeNotFound() {
        BazniCentarRequest request = Mockito.mock(BazniCentarRequest.class);
        given(troskovniCentarService.findById(request.getBazniCentarId())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> troskovniCentarController.addKontosFromKnjizenje(request));
    }

    @Test
    void deleteKontoFromProfitniCentar() {
        BazniKonto expected = Mockito.mock(BazniKonto.class);
        given(troskovniCentarService.findBazniKontoById(MOCK_ID)).willReturn(Optional.of(expected));
        ResponseEntity<?> responseEntity = troskovniCentarController.deleteKontoFromProfitniCentar(MOCK_ID);
        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteKontoFromProfitniCentarNotFound() {
        given(troskovniCentarService.findBazniKontoById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> troskovniCentarController.deleteKontoFromProfitniCentar(MOCK_ID));
    }
}
