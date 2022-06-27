package raf.si.racunovodstvo.knjizenje.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.services.BilansService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BilansRestControllerTest {

    @InjectMocks
    private BilansRestController bilansRestController;
    @Mock
    private BilansService bilansService;

    @Test
    void getBilans() {
        List<Date> datumiOd  = new ArrayList<>();
        List<Date> datumiDo  = new ArrayList<>();
        datumiOd.add(new Date());
        datumiDo.add(new Date());

        String brojKontaOd = "1";
        String brojKontaDo = "2";

        Map<String,List<BilansResponse>> bilansResponseListMap = new HashMap<>();
        bilansResponseListMap.put("",new ArrayList<>());
        bilansResponseListMap.get("").add(new BilansResponse(500.0, 500.0, 1L, "001", ""));

        given(bilansService.findBilans(any(Boolean.class), any(List.class), any(List.class))).willReturn(bilansResponseListMap);
        given(bilansService.findBrutoBilans(any(String.class), any(String.class), any(Date.class), any(Date.class))).willReturn(bilansResponseListMap.get(""));

        ResponseEntity<?> responseEntity1 = bilansRestController.getBilansStanja(datumiOd, datumiDo);
        assertEquals(200, responseEntity1.getStatusCodeValue());

        ResponseEntity<?> responseEntity2 = bilansRestController.getBilansUspeha(datumiOd, datumiDo);
        assertEquals(200, responseEntity2.getStatusCodeValue());

        ResponseEntity<?> responseEntity3 = bilansRestController.getBrutoBilans(brojKontaOd, brojKontaDo, datumiOd.get(0), datumiDo.get(0));
        assertEquals(200, responseEntity3.getStatusCodeValue());
    }

    @Test
    void getBilansNotFound() {
        List<Date> datumiOd  = new ArrayList<>();
        List<Date> datumiDo  = new ArrayList<>();
        datumiOd.add(new Date());
        datumiDo.add(new Date());

        String brojKontaOd = "1";
        String brojKontaDo = "2";

        ResponseEntity<?> responseEntity1 = bilansRestController.getBilansStanja(datumiOd, datumiDo);
        assertEquals(404, responseEntity1.getStatusCodeValue());

        ResponseEntity<?> responseEntity2 = bilansRestController.getBilansUspeha(datumiOd, datumiDo);
        assertEquals(404, responseEntity2.getStatusCodeValue());

        ResponseEntity<?> responseEntity3 = bilansRestController.getBrutoBilans(brojKontaOd, brojKontaDo, datumiOd.get(0), datumiDo.get(0));
        assertEquals(404, responseEntity3.getStatusCodeValue());
    }
}
