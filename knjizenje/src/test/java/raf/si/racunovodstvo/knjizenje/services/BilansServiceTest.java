package raf.si.racunovodstvo.knjizenje.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.knjizenje.repositories.KontnaGrupaRepository;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BilansServiceTest {

    @InjectMocks
    private BilansService bilansService;

    @Mock
    private KontnaGrupaRepository kontnaGrupaRepository;

    @Test
    void findBilans(){

        List<BilansResponse> bilansResponseList = new ArrayList<>();

        List<String> startsWith = new ArrayList<>();
        List<Date> datumiOd = new ArrayList<>();
        List<Date> datumiDo = new ArrayList<>();

        assertEquals(bilansResponseList, bilansService.findBilans(startsWith, datumiOd, datumiDo));
    }

    @Test
    void findBilansWithDates(){

        List<BilansResponse> bilansResponseList = new ArrayList<>();

        List<String> startsWith = new ArrayList<>();
        List<Date> datumiOd = new ArrayList<>();
        List<Date> datumiDo = new ArrayList<>();
        Date date1 = new Date();
        Date date2 = new Date();

        datumiOd.add(date1);
        datumiDo.add(date2);

        given(kontnaGrupaRepository.findAllStartingWith(startsWith, date1, date2))
                .willReturn(bilansResponseList);

        assertEquals(bilansResponseList, bilansService.findBilans(startsWith, datumiOd, datumiDo));
    }

    @Test
    void findBrutoBilans1(){
        List<BilansResponse> bilansResponseList = new ArrayList<>();
        BilansResponse bilansResponse = new BilansResponse(500.0, 500.0, 1L, "1L", "1");
        BilansResponse bilansResponse2 = new BilansResponse(1500.0, 1500.0, 1L, "2L", "2");
        bilansResponseList.add(bilansResponse);
        bilansResponseList.add(bilansResponse2);
        String brojKontaOd = new String();
        String brojKontaDo = new String();
        Date datumOd = new Date();
        Date datumDo = new Date();

        given(kontnaGrupaRepository.findAllForBilans(brojKontaOd, brojKontaDo, datumOd, datumDo))
                .willReturn(bilansResponseList);

        assertEquals(bilansResponseList, bilansService.findBrutoBilans
                (brojKontaOd, brojKontaDo, datumOd, datumDo));
    }

    @Test
    void findBrutoBilans2(){
        List<BilansResponse> bilansResponseList = new ArrayList<>();
        BilansResponse bilansResponse = new BilansResponse(500.0, 500.0, 1L, "1L", "123333");
        BilansResponse bilansResponse2 = new BilansResponse(1500.0, 1500.0, 1L, "2L", "12222");
        bilansResponseList.add(bilansResponse);
        bilansResponseList.add(bilansResponse2);
        String brojKontaOd = new String();
        String brojKontaDo = new String();
        Date datumOd = new Date();
        Date datumDo = new Date();

        given(kontnaGrupaRepository.findAllForBilans(brojKontaOd, brojKontaDo, datumOd, datumDo))
                .willReturn(bilansResponseList);

        assertEquals(bilansResponseList, bilansService.findBrutoBilans
                (brojKontaOd, brojKontaDo, datumOd, datumDo));
    }

    @Test
    void findBrutoBilans3(){
        List<BilansResponse> bilansResponseList = new ArrayList<>();
        BilansResponse bilansResponse = new BilansResponse(500.0, 500.0, 1L, "1L", "123");
        BilansResponse bilansResponse2 = new BilansResponse(1500.0, 1500.0, 1L, "2L", "61");
        bilansResponseList.add(bilansResponse);
        bilansResponseList.add(bilansResponse2);
        String brojKontaOd = new String();
        String brojKontaDo = new String();
        Date datumOd = new Date();
        Date datumDo = new Date();

        given(kontnaGrupaRepository.findAllForBilans(brojKontaOd, brojKontaDo, datumOd, datumDo))
                .willReturn(bilansResponseList);

        assertEquals(bilansResponseList, bilansService.findBrutoBilans
                (brojKontaOd, brojKontaDo, datumOd, datumDo));
    }


}
