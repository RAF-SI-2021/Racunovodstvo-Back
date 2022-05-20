package raf.si.racunovodstvo.preduzece.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.services.impl.ObracunZaposleniService;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ObracunZaposleniControllerTest {

    @InjectMocks
    private ObracunZaposleniController obracunZaposleniController;

    @Mock
    private ObracunZaposleniService obracunZaposleniService;

    @Test
    void createObracunZaposleniTest(){
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        given(obracunZaposleniService.save(1L, 0.5, 1L)).willReturn(obracunZaposleni);
        assertEquals(obracunZaposleni, obracunZaposleniController.createObracunZaposleni(1L, 0.5, 1L).getBody());
    }


    @Test
    void deleteObracunZaposleniTest(){
        obracunZaposleniController.deleteObracunZaposleni(1L);
        then(obracunZaposleniService).should(times(1)).deleteById(1L);
    }

    @Test
    void getAllObracunZaposleniTest(){
        List<ObracunZaposleni> obracunZaposlenis = new ArrayList<>();
        given(obracunZaposleniService.findAll()).willReturn(obracunZaposlenis);
        assertEquals(obracunZaposlenis, obracunZaposleniController.getAllObracunZaposleni().getBody());
    }

}