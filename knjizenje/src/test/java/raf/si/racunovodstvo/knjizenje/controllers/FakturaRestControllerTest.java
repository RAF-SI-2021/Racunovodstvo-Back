package raf.si.racunovodstvo.knjizenje.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.knjizenje.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.services.FakturaService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FakturaRestControllerTest {

    @InjectMocks
    private FakturaRestController fakturaRestController;
    @Mock
    private FakturaService fakturaService;
    @Mock
    private PreduzeceFeignClient preduzeceFeignClient;

    private static final String TOKEN = "token";

    private static final Long MOCK_ID = 1L;

    @Test
    void getSume() {
        String tipFakture = "tip";

        ResponseEntity<?> responseEntity = fakturaRestController.getSume(tipFakture);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getFakture() throws IOException {
        Integer page = 1;
        Integer size = 50;
        String[] sort = new String[1];
        sort[0] = "1";

        List<Faktura> fakture = new ArrayList<>();
        fakture.add(new Faktura());

        Pageable pageSort = Mockito.mock(Pageable.class);
        when(fakturaService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(fakture));

        ResponseEntity<?> responseEntity = fakturaRestController.getFakture(page, size, sort, TOKEN);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void search() throws IOException {
        given(fakturaService.findAll(any(Specification.class))).willReturn(new ArrayList());
        ResponseEntity<?> responseEntity = fakturaRestController.search("porez>1", TOKEN);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void searchException() {
        assertThrows(OperationNotSupportedException.class, () -> fakturaRestController.search("search", TOKEN));
    }

    @Test
    void createFaktura() throws IOException {
        Faktura faktura = new Faktura();
        faktura.setPreduzeceId(MOCK_ID);
        Preduzece preduzece = new Preduzece();
        preduzece.setPreduzeceId(1L);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_ID, TOKEN)).willReturn(ResponseEntity.ok(preduzece));

        ResponseEntity<?> responseEntity = fakturaRestController.createFaktura(faktura, TOKEN);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void createFakturaException() {
        Faktura faktura = new Faktura();
        assertThrows(PersistenceException.class, () -> fakturaRestController.createFaktura(faktura, TOKEN));
    }

    @Test
    void updateFaktura() throws IOException {
        Faktura faktura = new Faktura();
        faktura.setPreduzeceId(MOCK_ID);
        faktura.setDokumentId(MOCK_ID);
        Preduzece preduzece = new Preduzece();
        preduzece.setPreduzeceId(MOCK_ID);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_ID, TOKEN)).willReturn(ResponseEntity.ok(preduzece));
        given(fakturaService.findById(MOCK_ID)).willReturn(Optional.of(faktura));
        ResponseEntity<?> responseEntity = fakturaRestController.updateFaktura(faktura, TOKEN);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateFakturaEmpty() throws IOException {
        Faktura faktura = new Faktura();
        faktura.setPreduzeceId(MOCK_ID);
        faktura.setDokumentId(MOCK_ID);
        Preduzece preduzece = new Preduzece();
        preduzece.setPreduzeceId(MOCK_ID);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_ID, TOKEN)).willReturn(ResponseEntity.ok(preduzece));
        given(fakturaService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> fakturaRestController.updateFaktura(faktura, TOKEN));
    }

    @Test
    void updateFakturaException() throws IOException {
        Faktura faktura = new Faktura();
        faktura.setPreduzeceId(MOCK_ID);
        faktura.setDokumentId(MOCK_ID);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_ID, TOKEN)).willReturn(ResponseEntity.ok(null));
        lenient().when(fakturaService.findById(MOCK_ID)).thenReturn(Optional.of(faktura));
        assertThrows(PersistenceException.class, () -> fakturaRestController.updateFaktura(faktura, TOKEN));
    }

    @Test
    void deleteFaktura() {
        Faktura faktura = new Faktura();
        given(fakturaService.findById(MOCK_ID)).willReturn(Optional.of(faktura));

        ResponseEntity<?> responseEntity = fakturaRestController.deleteFaktura(MOCK_ID);
        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteFakturaException() {
        given(fakturaService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> fakturaRestController.deleteFaktura(MOCK_ID));
    }
}
