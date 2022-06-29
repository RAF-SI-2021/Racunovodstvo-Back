package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.converters.impl.ObracunZaposleniConverter;
import raf.si.racunovodstvo.preduzece.model.*;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;
import raf.si.racunovodstvo.preduzece.repositories.ObracunRepository;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaposleniRepository;
import raf.si.racunovodstvo.preduzece.repositories.PlataRepository;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaposleniRequest;
import raf.si.racunovodstvo.preduzece.specifications.RacunSpecification;
import raf.si.racunovodstvo.preduzece.specifications.SearchCriteria;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
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
    private ObracunRepository obracunRepository;
    @Mock
    private PlataRepository plataRepository;
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

    private static final String MOCK_SEARCH_KEY = "MOCK_KEY";
    private static final String MOCK_SEARCH_VALUE = "MOCK_VALUE";
    private static final String MOCK_SEARCH_OPERATION = "MOCK_OPERATION";

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
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);
        koeficijent.setNajnizaOsnovica(MOCK_NAJNIZA_OSNOVICA);
        koeficijent.setNajvisaOsnovica(MOCK_NAJVISA_OSNOVCA);


        given(koeficijentService.getCurrentKoeficijent()).willReturn(koeficijent);
        given(obracunZaposleniRepository.save(obracunZaposleni)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.findById(1L)).willReturn(Optional.of(obracunZaposleni));

        ObracunZaposleni result = obracunZaposleniService.update(MOCK_UCINAK, MOCK_NETO_PLATA, 1L);
        assertEquals(obracunZaposleni, result);
        assertEquals(MOCK_UCINAK, result.getUcinak());
        assertEquals(MOCK_NETO_PLATA, result.getNetoPlata());

    }

    @Test
    void updateNetoPlataNullTest(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);
        obracunZaposleni.setNetoPlata(MOCK_NETO_PLATA);
        koeficijent.setNajnizaOsnovica(MOCK_NAJNIZA_OSNOVICA);
        koeficijent.setNajvisaOsnovica(MOCK_NAJVISA_OSNOVCA);


        given(koeficijentService.getCurrentKoeficijent()).willReturn(koeficijent);
        given(obracunZaposleniRepository.save(obracunZaposleni)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.findById(1L)).willReturn(Optional.of(obracunZaposleni));

        ObracunZaposleni result = obracunZaposleniService.update(MOCK_UCINAK, null, 1L);
        assertEquals(obracunZaposleni, result);
        assertEquals(MOCK_UCINAK, result.getUcinak());
        assertEquals(MOCK_NETO_PLATA, result.getNetoPlata());
    }

    @Test
    void updateUcinakNullTest(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);
        obracunZaposleni.setUcinak(MOCK_UCINAK);
        koeficijent.setNajnizaOsnovica(MOCK_NAJNIZA_OSNOVICA);
        koeficijent.setNajvisaOsnovica(MOCK_NAJVISA_OSNOVCA);


        given(koeficijentService.getCurrentKoeficijent()).willReturn(koeficijent);
        given(obracunZaposleniRepository.save(obracunZaposleni)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.findById(1L)).willReturn(Optional.of(obracunZaposleni));

        ObracunZaposleni result = obracunZaposleniService.update(null, MOCK_NETO_PLATA, 1L);
        assertEquals(obracunZaposleni, result);
        assertEquals(MOCK_UCINAK, result.getUcinak());
        assertEquals(MOCK_NETO_PLATA, result.getNetoPlata());
    }


    @Test
    void updateExceptionTest(){
        given(obracunZaposleniRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> obracunZaposleniService.update(0.5, null, 1L));
        then(obracunZaposleniRepository).should(never()).save(any());
    }

    @Test
    void findAllTest1(){
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        List<ObracunZaposleni> obracunZaposleniList = new ArrayList<>();
        obracunZaposleniList.add(obracunZaposleni);

        Page<ObracunZaposleni> page = new PageImpl<>(obracunZaposleniList);
        Specification<ObracunZaposleni> specification =
                new RacunSpecification<>(new SearchCriteria(MOCK_SEARCH_KEY, MOCK_SEARCH_VALUE, MOCK_SEARCH_OPERATION));
        Pageable pageable = PageRequest.of(0, 20);

        given(obracunZaposleniRepository.findAll(specification, pageable)).willReturn(page);
        assertEquals(page, obracunZaposleniService.findAll(specification, pageable));
    }

    @Test
    void findAllTest2(){
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        List<ObracunZaposleni> obracunZaposleniList = new ArrayList<>();
        obracunZaposleniList.add(obracunZaposleni);

        Page<ObracunZaposleni> page = new PageImpl<>(obracunZaposleniList);
        Pageable pageable = PageRequest.of(0, 20);

        given(obracunZaposleniRepository.findAll(pageable)).willReturn(page);
        assertEquals(page, obracunZaposleniService.findAll(pageable));
    }

    @Test
    void makeObracun(){
        Obracun obracun = new Obracun();
        obracun.setObracunId(1L);
        Date date = new Date();
        Plata plata = new Plata();
        Zaposleni zaposleni = new Zaposleni();
        zaposleni.setZaposleniId(1L);
        plata.setZaposleni(zaposleni);
        List<Plata> plate = new ArrayList<>();
        plate.add(plata);

        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        obracunZaposleni.setObracun(obracun);
        obracunZaposleni.setZaposleni(zaposleni);
        obracunZaposleni.setNetoPlata(MOCK_NETO_PLATA);
        obracunZaposleni.setUcinak(MOCK_UCINAK);
        koeficijent.setNajnizaOsnovica(MOCK_NAJNIZA_OSNOVICA);
        koeficijent.setNajvisaOsnovica(MOCK_NAJVISA_MANJA_OSNOVICA);

        given(plataRepository.findPlataByDatumAndStatusZaposlenog(date, StatusZaposlenog.ZAPOSLEN)).willReturn(plate);

        given(koeficijentService.getCurrentKoeficijent()).willReturn(koeficijent);
        given(obracunZaposleniConverter.convert(any(ObracunZaposleniRequest.class))).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.save(obracunZaposleni)).willReturn(obracunZaposleni);
        given(obracunZaposleniRepository.findByZaposleniAndObracun(zaposleni, obracun)).willReturn(Optional.empty());

        given(obracunRepository.save(any(Obracun.class))).willReturn(obracun);

        assertEquals(obracun, obracunZaposleniService.makeObracun(date, 1L));
    }
}