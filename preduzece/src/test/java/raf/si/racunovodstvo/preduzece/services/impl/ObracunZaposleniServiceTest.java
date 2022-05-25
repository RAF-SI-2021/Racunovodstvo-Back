package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import raf.si.racunovodstvo.preduzece.converters.impl.ObracunZaposleniConverter;
import raf.si.racunovodstvo.preduzece.model.*;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaposleniRepository;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaposleniRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ObracunZaposleniServiceTest {

    @InjectMocks
    private ObracunZaposleniService obracunZaposleniService;

    @Mock
    private ObracunZaposleniRepository obracunZaposleniRepository;
    @Mock
    private ObracunZaposleniConverter obracunZaposleniConverter;
    @Mock
    private KoeficijentService koeficijentService;

    private static final Double MOCK_NETO_PLATA = 200.0;
    private static final Double MOCK_UCINAK = 0.5;
    private static final Double MOCK_NAJNIZA_OSNOVICA = 99.0;
    private static final Double MOCK_NAJVISA_OSNOVCA = 105.0;
    private static final Double MOCK_NAJNIZA_VECA_OSNOVICA = 101.0;
    private static final Double MOCK_NAJVISA_MANJA_OSNOVICA = 99.0;
    private Koeficijent koeficijent;

    @BeforeEach
    void setUp(){
        koeficijent = new Koeficijent();
        koeficijent.setPenzionoOsiguranje1(100.00);
        koeficijent.setNezaposlenost1(5.0);
        koeficijent.setNezaposlenost2(10.0);
        koeficijent.setPenzionoOsiguranje2(150.0);
        koeficijent.setZdravstvenoOsiguranje1(100.0);
        koeficijent.setZdravstvenoOsiguranje2(120.0);
        koeficijent.setKoeficijentPoreza(5.0);
        koeficijent.setPoreskoOslobadjanje(2.0);
    }

    @Test
    void saveTest(){
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        given(obracunZaposleniRepository.save(obracunZaposleni)).willReturn(obracunZaposleni);
        assertEquals(obracunZaposleni, obracunZaposleniService.save(obracunZaposleni));
    }

    @Test
    void findByIdTest(){
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        given(obracunZaposleniRepository.findById(1L)).willReturn(Optional.of(obracunZaposleni));
        assertEquals(obracunZaposleni, obracunZaposleniService.findById(1L).get());
    }

    @Test
    void findAllTest(){
        List<ObracunZaposleni> obracunZaposlenis = new ArrayList<>();
        given(obracunZaposleniRepository.findAll()).willReturn(obracunZaposlenis);
        assertEquals(obracunZaposlenis, obracunZaposleniService.findAll());
    }

    @Test
    void deleteByIdSuccessTest(){
        given(obracunZaposleniRepository.findById(1L)).willReturn(Optional.of(new ObracunZaposleni()));
        obracunZaposleniService.deleteById(1L);
        then(obracunZaposleniRepository).should(times(1)).deleteById(1L);
    }

    @Test
    void deleteByIdExceptionTest(){
        given(obracunZaposleniRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> obracunZaposleniService.deleteById(1L));
        then(obracunZaposleniRepository).should(never()).deleteById(1L);
    }

    @Test
    void saveCustomSuccessTest(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        ObracunZaposleniRequest obracunZaposleniRequest = new ObracunZaposleniRequest();
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);
        obracunZaposleni.setNetoPlata(MOCK_NETO_PLATA);
        obracunZaposleni.setUcinak(MOCK_UCINAK);
        koeficijent.setNajnizaOsnovica(MOCK_NAJNIZA_OSNOVICA);
        koeficijent.setNajvisaOsnovica(MOCK_NAJVISA_OSNOVCA);


        given(koeficijentService.getCurrentKoeficijent()).willReturn(koeficijent);
        given(obracunZaposleniConverter.convert(obracunZaposleniRequest)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.save(obracunZaposleni)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.findByZaposleniAndObracun(zaposleni, obracun)).willReturn(Optional.empty());

        ObracunZaposleni result = obracunZaposleniService.save(obracunZaposleniRequest);
        assertEquals(obracunZaposleni, result);
    }

    @Test
    void saveCustomSuccessTest2(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        ObracunZaposleniRequest obracunZaposleniRequest = new ObracunZaposleniRequest();
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);
        obracunZaposleni.setNetoPlata(MOCK_NETO_PLATA);
        obracunZaposleni.setUcinak(MOCK_UCINAK);
        koeficijent.setNajnizaOsnovica(MOCK_NAJNIZA_VECA_OSNOVICA);
        koeficijent.setNajvisaOsnovica(MOCK_NAJVISA_OSNOVCA);

        given(koeficijentService.getCurrentKoeficijent()).willReturn(koeficijent);
        given(obracunZaposleniConverter.convert(obracunZaposleniRequest)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.save(obracunZaposleni)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.findByZaposleniAndObracun(zaposleni, obracun)).willReturn(Optional.empty());

        ObracunZaposleni result = obracunZaposleniService.save(obracunZaposleniRequest);
        assertEquals(obracunZaposleni, result);
    }

    @Test
    void saveCustomSuccessTest3(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        ObracunZaposleniRequest obracunZaposleniRequest = new ObracunZaposleniRequest();
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);
        obracunZaposleni.setNetoPlata(MOCK_NETO_PLATA);
        obracunZaposleni.setUcinak(MOCK_UCINAK);
        koeficijent.setNajnizaOsnovica(MOCK_NAJNIZA_OSNOVICA);
        koeficijent.setNajvisaOsnovica(MOCK_NAJVISA_MANJA_OSNOVICA);


        given(koeficijentService.getCurrentKoeficijent()).willReturn(koeficijent);
        given(obracunZaposleniConverter.convert(obracunZaposleniRequest)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.save(obracunZaposleni)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.findByZaposleniAndObracun(zaposleni, obracun)).willReturn(Optional.empty());

        ObracunZaposleni result = obracunZaposleniService.save(obracunZaposleniRequest);
        assertEquals(obracunZaposleni, result);
    }

    @Test
    void saveCustomObracunZaposleniExistsTest(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        ObracunZaposleniRequest obracunZaposleniRequest = new ObracunZaposleniRequest();
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);

        given(obracunZaposleniConverter.convert(obracunZaposleniRequest)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.findByZaposleniAndObracun(zaposleni, obracun)).willReturn(Optional.of(obracunZaposleni));

        assertThrows(EntityExistsException.class, () -> obracunZaposleniService.save(obracunZaposleniRequest));
        then(obracunZaposleniRepository).should(never()).save(any());
    }

    @Test
    void updateTest(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        ObracunZaposleniRequest obracunZaposleniRequest = new ObracunZaposleniRequest();
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);
        obracunZaposleni.setNetoPlata(MOCK_NETO_PLATA);
        obracunZaposleni.setUcinak(MOCK_UCINAK);
        obracunZaposleniRequest.setObracunZaposleniId(1L);
        koeficijent.setNajnizaOsnovica(MOCK_NAJNIZA_OSNOVICA);
        koeficijent.setNajvisaOsnovica(MOCK_NAJVISA_OSNOVCA);


        given(koeficijentService.getCurrentKoeficijent()).willReturn(koeficijent);
        given(obracunZaposleniConverter.convert(obracunZaposleniRequest)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.save(obracunZaposleni)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.findById(1L)).willReturn(Optional.of(obracunZaposleni));

        ObracunZaposleni result = obracunZaposleniService.update(obracunZaposleniRequest);
        assertEquals(obracunZaposleni, result);

    }

    @Test
    void updateExceptionTest(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        ObracunZaposleniRequest obracunZaposleniRequest = new ObracunZaposleniRequest();
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);
        obracunZaposleni.setNetoPlata(MOCK_NETO_PLATA);
        obracunZaposleni.setUcinak(MOCK_UCINAK);
        obracunZaposleniRequest.setObracunZaposleniId(1L);
        koeficijent.setNajnizaOsnovica(MOCK_NAJNIZA_OSNOVICA);
        koeficijent.setNajvisaOsnovica(MOCK_NAJVISA_OSNOVCA);

        given(obracunZaposleniRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> obracunZaposleniService.update(obracunZaposleniRequest));
        then(obracunZaposleniRepository).should(never()).save(obracunZaposleni);

    }
}