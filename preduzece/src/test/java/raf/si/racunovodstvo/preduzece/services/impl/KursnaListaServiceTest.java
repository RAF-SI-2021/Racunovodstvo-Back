package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.preduzece.feign.KursnaListaFeignClient;
import raf.si.racunovodstvo.preduzece.responses.KursnaListaResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KursnaListaServiceTest {

    @InjectMocks
    private KursnaListaService kursnaListaService;
    @Mock
    private KursnaListaFeignClient kursnaListaFeignClient;

    @Test
    void getKursnaLista() {
        KursnaListaResponse response = new KursnaListaResponse();

        given(kursnaListaFeignClient.getKursnaListaForDate(any(String.class))).willReturn(response);
        assertEquals(response, kursnaListaService.getKursnaLista());
    }

    @Test
    void getKursnaListaForDay() {
        KursnaListaResponse response = new KursnaListaResponse();

        given(kursnaListaFeignClient.getKursnaListaForDate(any(String.class))).willReturn(response);
        assertEquals(response, kursnaListaService.getKursnaListaForDay("date"));
    }
}