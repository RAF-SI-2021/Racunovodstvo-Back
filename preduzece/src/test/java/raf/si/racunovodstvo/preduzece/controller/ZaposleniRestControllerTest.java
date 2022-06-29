package raf.si.racunovodstvo.preduzece.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.responses.ZaposleniResponse;
import raf.si.racunovodstvo.preduzece.services.impl.ZaposleniService;
import raf.si.racunovodstvo.preduzece.specifications.RacunSpecificationsBuilder;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ZaposleniRestControllerTest {

    @InjectMocks
    private ZaposleniRestController zaposleniRestController;
    @Mock
    private ZaposleniService zaposleniService;

    private static final Long MOCK_ID = 1L;

    @Test
    void createZaposleni() {
        Zaposleni zaposleni = new Zaposleni();
        ResponseEntity<?> responseEntity = zaposleniRestController.createZaposleni(zaposleni);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateZaposleni() {
        ZaposleniResponse zaposleniResponse = new ZaposleniResponse();
        Zaposleni zaposleni = new Zaposleni();
        given(zaposleniService.findZaposleniById(MOCK_ID)).willReturn(Optional.of(zaposleniResponse));
        ResponseEntity<?> responseEntity = zaposleniRestController.updateZaposleni(zaposleni, MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateZaposleniException() {
        Zaposleni zaposleni = new Zaposleni();
        given(zaposleniService.findZaposleniById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> zaposleniRestController.updateZaposleni(zaposleni, MOCK_ID));
    }

    @Test
    void zaposleniOtkaz() {
        Zaposleni zaposleni = new Zaposleni();
        given(zaposleniService.findById(MOCK_ID)).willReturn(Optional.of(zaposleni));

        ResponseEntity<?> responseEntity = zaposleniRestController.zaposleniOtkaz(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void zaposleniOtkazException() {
        given(zaposleniService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> zaposleniRestController.zaposleniOtkaz(MOCK_ID));
    }

    @Test
    void getZaposleniId() {
        ZaposleniResponse zaposleniResponse = new ZaposleniResponse();
        given(zaposleniService.findZaposleniById(MOCK_ID)).willReturn(Optional.of(zaposleniResponse));

        ResponseEntity<?> responseEntity = zaposleniRestController.getZaposleniId(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getZaposleniIdException() {
        given(zaposleniService.findZaposleniById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> zaposleniRestController.getZaposleniId(MOCK_ID));
    }

    @Test
    void search() {
        String search = "abc";
        given(zaposleniService.findAll(null)).willReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> zaposleniRestController.search(search));
    }

    @Test
    void search2() {
        List<ZaposleniResponse> zaposleniList = new ArrayList<>();
        zaposleniList.add(new ZaposleniResponse());
        String search = "abc";
        given(zaposleniService.findAll(null)).willReturn(zaposleniList);
        ResponseEntity<?> responseEntity = zaposleniRestController.search(search);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void search3() {
        List<ZaposleniResponse> zaposleniList = new ArrayList<>();
        zaposleniList.add(new ZaposleniResponse());
        String search = "abc";
        Matcher mock = mock(Matcher.class);
        lenient().when(mock.find()).thenReturn(true);
        lenient().when(zaposleniService.findAll(null)).thenReturn(zaposleniList);
        ResponseEntity<?> responseEntity = zaposleniRestController.search(search);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
