package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.preduzece.feign.TransakcijeFeignClient;
import raf.si.racunovodstvo.preduzece.model.*;
import raf.si.racunovodstvo.preduzece.repositories.ObracunRepository;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaposleniRepository;
import raf.si.racunovodstvo.preduzece.requests.ObracunTransakcijeRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObracunServiceTest {

    @InjectMocks
    private ObracunService obracunService;
    @Mock
    private ObracunRepository obracunRepository;
    @Mock
    private TransakcijeFeignClient transakcijeFeignClient;


    private static final Long MOCK_ID = 1L;
    private static final String MOCK_NAZIV = "Naziv";
    private static final String MOCK_TOKEN = "token";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Obracun getObracun(){
        Obracun obracun = new Obracun();
        obracun.setSifraTransakcije(1L);

        List<ObracunZaposleni> list = new ArrayList<>();
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();

        Zaposleni zaposleni = new Zaposleni();
        zaposleni.setZaposleniId(MOCK_ID);

        Preduzece preduzece = new Preduzece();
        preduzece.setPreduzeceId(MOCK_ID);

        zaposleni.setPreduzece(preduzece);

        obracunZaposleni.setZaposleni(zaposleni);
        obracunZaposleni.setObracun(obracun);
        list.add(obracunZaposleni);

        obracun.setObracunZaposleniList(list);

        return obracun;
    }

    @Test
    void save(){
        Obracun obracun = new Obracun();
        given(obracunRepository.save(obracun)).willReturn(obracun);

        assertEquals(obracun,obracunService.save(obracun));
    }

    @Test
    void findById(){
        Obracun obracun = new Obracun();
        given(obracunRepository.findById(MOCK_ID)).willReturn(Optional.of(obracun));

        assertEquals(obracun,obracunService.findById(MOCK_ID).get());
    }

    @Test
    void findAll(){
        List<Obracun> obracunList = new ArrayList<>();
        given(obracunRepository.findAll()).willReturn(obracunList);

        assertEquals(obracunList, obracunService.findAll());
    }

    @Test
    void deleteById(){
        obracunService.deleteById(MOCK_ID);
        then(obracunRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void obradiObracunException1(){
        given(obracunService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(RuntimeException.class, ()-> obracunService.obradiObracun(MOCK_ID,MOCK_TOKEN));
    }

    @Test
    void obradiObracunException2(){
        Obracun obracun = new Obracun();
        obracun.setObradjen(true);
        given(obracunService.findById(MOCK_ID)).willReturn(Optional.of(obracun));

        assertThrows(RuntimeException.class, ()-> obracunService.obradiObracun(MOCK_ID,MOCK_TOKEN));
    }

    @Test
    void obradiObracunException3(){
        Obracun obracun = new Obracun();
        obracun.setSifraTransakcije(0L);
        given(obracunService.findById(MOCK_ID)).willReturn(Optional.of(obracun));

        assertThrows(RuntimeException.class, ()-> obracunService.obradiObracun(MOCK_ID,MOCK_TOKEN));
    }

    @Test
    void obradiObracunException4(){
        given(obracunService.findById(MOCK_ID)).willReturn(Optional.of(getObracun()));
        lenient().when(transakcijeFeignClient.obracunZaradeTransakcije(any(List.class), any(String.class)))
                .thenReturn(new ResponseEntity(HttpStatus.BAD_REQUEST));

        assertThrows(RuntimeException.class, ()-> obracunService.obradiObracun(MOCK_ID,MOCK_TOKEN));
    }

    @Test
    void obradiObracunException5(){
        given(obracunService.findById(MOCK_ID)).willReturn(Optional.of(getObracun()));
        lenient().when(transakcijeFeignClient.obracunZaradeTransakcije(any(List.class), any(String.class)))
                .thenReturn(new ResponseEntity(HttpStatus.OK));

        assertThrows(RuntimeException.class, ()-> obracunService.obradiObracun(MOCK_ID,MOCK_TOKEN));
    }

    @Test
    void obradiObracunException6(){
        List<Transakcija> response = new ArrayList<>();
        Transakcija transakcija = new Transakcija();
        transakcija.setBrojTransakcije("1-2");
        response.add(transakcija);

        given(obracunService.findById(MOCK_ID)).willReturn(Optional.of(getObracun()));
        lenient().when(transakcijeFeignClient.obracunZaradeTransakcije(any(List.class), any(String.class)))
                .thenReturn(ResponseEntity.ok(response));

        assertThrows(RuntimeException.class, ()-> obracunService.obradiObracun(MOCK_ID,MOCK_TOKEN));
    }


    @Test
    void obradiObracun(){
        Obracun obracun = getObracun();
        List<Transakcija> response = new ArrayList<>();
        Transakcija transakcija = new Transakcija();
        transakcija.setBrojTransakcije("1-1");
        response.add(transakcija);

        given(obracunRepository.save(any(Obracun.class))).willReturn(obracun);
        given(obracunService.findById(MOCK_ID)).willReturn(Optional.of(obracun));
        lenient().when(transakcijeFeignClient.obracunZaradeTransakcije(any(List.class), any(String.class)))
                .thenReturn(ResponseEntity.ok(response));

        assertEquals(obracun, obracunService.obradiObracun(MOCK_ID,MOCK_TOKEN));
    }


    @Test
    void updateObracunZaradeNaziv(){
        Obracun obracun = new Obracun();
        String naziv = obracun.getNaziv();
        given(obracunRepository.findById(MOCK_ID)).willReturn(Optional.of(obracun));

        obracunService.updateObracunZaradeNaziv(MOCK_ID,MOCK_NAZIV);
        then(obracunRepository).should(times(1)).save(obracun);

        assertNotEquals(naziv,obracun.getNaziv());
    }
}