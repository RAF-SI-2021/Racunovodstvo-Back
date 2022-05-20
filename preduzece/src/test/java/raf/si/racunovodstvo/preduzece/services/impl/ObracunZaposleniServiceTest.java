package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.model.Plata;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.repositories.ObracunRepository;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaposleniRepository;

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
    private PlataService plataService;

    @Mock
    private ZaposleniService zaposleniService;

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
        obracun.setDatumObracuna(new Date());
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        Plata plata = new Plata();
        plata.setNetoPlata(100.0);

        given(zaposleniService.findById(1L)).willReturn(Optional.of(zaposleni));
        given(obracunRepository.findById(1L)).willReturn(Optional.of(obracun));
        given(plataService.findPlatabyDatumAndZaposleni(obracun.getDatumObracuna(), zaposleni)).willReturn(plata);
        given(obracunZaposleniRepository.findByZaposleniAndObracun(zaposleni, obracun)).willReturn(null);
        given(obracunZaposleniRepository.save(any())).willReturn(obracunZaposleni);

        ObracunZaposleni result = obracunZaposleniService.save(1L, 0.5, 1L);
        assertEquals(obracunZaposleni, result);
    }

    @Test
    void saveCustomObracunNotExistsTest(){
        given(zaposleniService.findById(1L)).willReturn(Optional.of(new Zaposleni()));
        given(obracunRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> obracunZaposleniService.save(1L, 0.5, 1L));
        then(obracunZaposleniRepository).should(never()).save(any());
    }

    @Test
    void saveCustomZaposleniNotExistsTest(){
        given(zaposleniService.findById(1L)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> obracunZaposleniService.save(1L, 0.5, 1L));
        then(obracunZaposleniRepository).should(never()).save(any());
    }

    @Test
    void saveCustomPlataNotExistsTest(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        obracun.setDatumObracuna(new Date());

        given(zaposleniService.findById(1L)).willReturn(Optional.of(zaposleni));
        given(obracunRepository.findById(1L)).willReturn(Optional.of(obracun));
        given(plataService.findPlatabyDatumAndZaposleni(obracun.getDatumObracuna(), zaposleni)).willReturn(null);

        assertThrows(EntityNotFoundException.class, () -> obracunZaposleniService.save(1L, 0.5, 1L));
        then(obracunZaposleniRepository).should(never()).save(any());
    }

    @Test
    void saveCustomObracunZaposleniExistsTest(){
        Zaposleni zaposleni = new Zaposleni();
        Obracun obracun = new Obracun();
        obracun.setDatumObracuna(new Date());
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();

        given(zaposleniService.findById(1L)).willReturn(Optional.of(zaposleni));
        given(obracunRepository.findById(1L)).willReturn(Optional.of(obracun));
        given(obracunZaposleniRepository.findByZaposleniAndObracun(zaposleni, obracun)).willReturn(obracunZaposleni);

        assertThrows(EntityExistsException.class, () -> obracunZaposleniService.save(1L, 0.5, 1L));
        then(obracunZaposleniRepository).should(never()).save(any());
    }
}