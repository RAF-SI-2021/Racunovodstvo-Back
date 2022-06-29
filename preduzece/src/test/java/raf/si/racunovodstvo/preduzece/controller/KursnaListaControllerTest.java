package raf.si.racunovodstvo.preduzece.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.preduzece.responses.KursnaListaResponse;
import raf.si.racunovodstvo.preduzece.services.IKursnaListaService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KursnaListaControllerTest {

    @InjectMocks
    private KursnaListaController kursnaListaController;
    @Mock
    private IKursnaListaService kursnaListaService;

    @Test
    void getKursnaLista() {
        KursnaListaResponse kursnaListaResponse = new KursnaListaResponse();

        given(kursnaListaService.getKursnaLista()).willReturn(kursnaListaResponse);

        assertEquals(HttpStatus.OK, kursnaListaController.getKursnaLista(null).getStatusCode());
    }
}