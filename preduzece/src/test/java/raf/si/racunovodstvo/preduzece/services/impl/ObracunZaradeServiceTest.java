package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import raf.si.racunovodstvo.preduzece.model.ObracunZarade;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaposleniRepository;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaradeRepository;
import raf.si.racunovodstvo.preduzece.repositories.PlataRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObracunZaradeServiceTest {

    @InjectMocks
    private ObracunZaradeService obracunZaradeService;

    @Mock
    private ObracunZaradeRepository obracunZaradeRepository;

    @Mock
    private PlataRepository plataRepository;

    @Mock
    private ObracunZaposleniRepository obracunZaposleniRepository;

    private static final Date MOCK_DATE = new Date();
    private static final Long MOCK_ID = 1l;
    private static final String MOCK_NAZIV = "Naziv";

    @Test
    void findByDate(){
        List<ObracunZarade> obracunZaradeList = new ArrayList<>();
        given((obracunZaradeRepository.findAllByDate(MOCK_DATE))).willReturn(obracunZaradeList);

        assertEquals(obracunZaradeList,obracunZaradeService.findByDate(MOCK_DATE));
    }

    @Test
    void save(){
        ObracunZarade obracunZarade = new ObracunZarade();
        given(obracunZaradeRepository.save(obracunZarade)).willReturn(obracunZarade);

        assertEquals(obracunZarade, obracunZaradeService.save(obracunZarade));
    }

    @Test
    void findById(){
        ObracunZarade obracunZarade = new ObracunZarade();
        given(obracunZaradeRepository.findById(MOCK_ID)).willReturn(Optional.of(obracunZarade));

        assertEquals(obracunZarade, obracunZaradeService.findById(MOCK_ID).get());
    }

    @Test
    void findByIdException(){
        given(obracunZaradeRepository.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> obracunZaradeService.findById(MOCK_ID).get());
    }

    @Test
    void findAll(){
        List<ObracunZarade> obracunZaradeList = new ArrayList<>();
        given((obracunZaradeRepository.findAll())).willReturn(obracunZaradeList);

        assertEquals(obracunZaradeList,obracunZaradeService.findAll());
    }

    @Test
    void deleteById(){
        obracunZaradeService.deleteById(MOCK_ID);
        then(obracunZaradeRepository).should(times(0)).deleteById(MOCK_ID);
    }

    /*@Test
    void makeObracunZarade(){
        ObracunZarade obracunZarade = new ObracunZarade();
        obracunZaradeService.makeObracunZarade(MOCK_DATE);
        then(obracunZaradeRepository).should(atLeast(0)).save(obracunZarade);
    }*/

    @Test
    void updateObracunZaradeNaziv(){
        ObracunZarade obracunZarade = new ObracunZarade();
        String naziv = obracunZarade.getNaziv();
        given(obracunZaradeRepository.findById(MOCK_ID)).willReturn(Optional.of(obracunZarade));

        obracunZaradeService.updateObracunZaradeNaziv(MOCK_ID,MOCK_NAZIV);
        then(obracunZaradeRepository).should(times(1)).save(obracunZarade);

        assertNotEquals(naziv,obracunZarade.getNaziv());
    }

    @Test
    void updateObracunZaradeNazivExc(){
        given(obracunZaradeRepository.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> obracunZaradeService.updateObracunZaradeNaziv(MOCK_ID, MOCK_NAZIV));
    }
}