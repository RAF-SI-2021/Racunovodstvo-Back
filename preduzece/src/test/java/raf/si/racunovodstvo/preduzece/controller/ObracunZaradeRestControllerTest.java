package raf.si.racunovodstvo.preduzece.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.preduzece.feign.TransakcijeFeignClient;
import raf.si.racunovodstvo.preduzece.jobs.ObracunZaradeJob;
import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaradeConfigRequest;
import raf.si.racunovodstvo.preduzece.responses.ObracunZaradeConfigResponse;
import raf.si.racunovodstvo.preduzece.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.preduzece.services.impl.ObracunZaposleniService;
import raf.si.racunovodstvo.preduzece.services.impl.ZaposleniService;

import javax.persistence.EntityNotFoundException;

import java.time.DateTimeException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObracunZaradeRestControllerTest {

    @InjectMocks
    private ObracunZaradeRestController obracunZaradeRestController;
    @Mock
    private ObracunZaposleniService obracunZaposleniService;
    @Mock
    private ObracunZaradeJob obracunZaradeJob;
    @Mock
    private TransakcijeFeignClient transakcijeFeignClient;

    private static final Long MOCK_ID = 1L;
    private static final String TOKEN = "token";


    @Test
    void setConfig() {
        ObracunZaradeConfigRequest req = new ObracunZaradeConfigRequest();
        req.setSifraTransakcijeId(MOCK_ID);
        req.setDayOfMonth(5);

        given(transakcijeFeignClient.getById(MOCK_ID, TOKEN)).willReturn(ResponseEntity.ok(new SifraTransakcijeResponse()));

        assertEquals(HttpStatus.OK, obracunZaradeRestController.setConfig(req, TOKEN).getStatusCode());
    }

    @Test
    void setConfigExc1() {
        ObracunZaradeConfigRequest req = new ObracunZaradeConfigRequest();
        req.setSifraTransakcijeId(MOCK_ID);
        req.setDayOfMonth(5);

        given(transakcijeFeignClient.getById(MOCK_ID, TOKEN)).willReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        assertThrows(EntityNotFoundException.class, () -> obracunZaradeRestController.setConfig(req, TOKEN));
    }

    @Test
    void setConfigExc2() {
        ObracunZaradeConfigRequest req = new ObracunZaradeConfigRequest();
        req.setSifraTransakcijeId(MOCK_ID);
        req.setDayOfMonth(5);

        given(transakcijeFeignClient.getById(MOCK_ID, TOKEN)).willReturn(ResponseEntity.ok(new SifraTransakcijeResponse()));
        doThrow(DateTimeException.class).when(obracunZaradeJob).setDayOfMonth(req.getDayOfMonth());
        assertEquals(HttpStatus.BAD_REQUEST, obracunZaradeRestController.setConfig(req, TOKEN).getStatusCode());
    }

    @Test
    void getConfig() {
        given(obracunZaradeJob.getSifraTransakcijeId()).willReturn(1L);
        given(obracunZaradeJob.getDayOfMonth()).willReturn(5);

        given(transakcijeFeignClient.getById(1L, TOKEN)).willReturn(ResponseEntity.ok(new SifraTransakcijeResponse()));
        assertEquals(HttpStatus.OK, obracunZaradeRestController.getConfig(TOKEN).getStatusCode());
    }

    @Test
    void getConfigExc1() {
        given(obracunZaradeJob.getSifraTransakcijeId()).willReturn(0L);
        assertThrows(RuntimeException.class, () -> obracunZaradeRestController.getConfig(TOKEN));
    }

    @Test
    void getConfigExc2() {
        given(obracunZaradeJob.getSifraTransakcijeId()).willReturn(1L);
        given(transakcijeFeignClient.getById(1L, TOKEN)).willReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        assertThrows(EntityNotFoundException.class, () -> obracunZaradeRestController.getConfig(TOKEN));
    }

    @Test
    void createObracunZarade() {
        Date date = new Date();
        given(obracunZaposleniService.makeObracun(date, 1L)).willReturn(new Obracun());
        assertEquals(HttpStatus.OK, obracunZaradeRestController.createObracunZarade(date, 1L).getStatusCode());

    }
}