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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import raf.si.racunovodstvo.knjizenje.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
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
    private RestTemplate restTemplate;

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
        String body = "{\"preduzeceId\":1}";
        given(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))).willReturn(ResponseEntity.ok(body));

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
        String body = "{\"preduzeceId\":1}";
        given(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))).willReturn(ResponseEntity.ok(body));
        given(fakturaService.findById(MOCK_ID)).willReturn(Optional.of(faktura));
        ResponseEntity<?> responseEntity = fakturaRestController.updateFaktura(faktura, TOKEN);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateFakturaEmpty() throws IOException {
        Faktura faktura = new Faktura();
        faktura.setPreduzeceId(MOCK_ID);
        faktura.setDokumentId(MOCK_ID);
        String body = "{\"preduzeceId\":1}";
        given(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))).willReturn(ResponseEntity.ok(body));
        given(fakturaService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> fakturaRestController.updateFaktura(faktura, TOKEN));
    }

    @Test
    void updateFakturaException() throws IOException {
        Faktura faktura = new Faktura();
        faktura.setPreduzeceId(MOCK_ID);
        faktura.setDokumentId(MOCK_ID);
        String body = "null";
        lenient().when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))).thenReturn(ResponseEntity.ok(body));
        lenient().when(fakturaService.findById(MOCK_ID)).thenReturn(Optional.of(faktura));
        assertThrows(PersistenceException.class, () -> fakturaRestController.updateFaktura(faktura, TOKEN));
    }

    @Test
    void deleteFaktura() {
        Faktura faktura = new Faktura();
        given(fakturaService.findById(MOCK_ID)).willReturn(Optional.of(faktura));

        ResponseEntity<?> responseEntity = fakturaRestController.deleteFaktura(MOCK_ID);
        assertEquals(responseEntity.getStatusCodeValue(), 204);
    }

    @Test
    void deleteFakturaException() {
        given(fakturaService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> fakturaRestController.deleteFaktura(MOCK_ID));
    }
}