package raf.si.racunovodstvo.knjizenje.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.SifraTransakcijeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SifraTransakcijeServiceTest {

    @InjectMocks
    private SifraTransakcijeService sifraTransakcijeService;

    @Mock
    private SifraTransakcijeRepository sifraTransakcijeRepository;

    @Test
    void saveTest() {
        SifraTransakcije sifraTransakcije = new SifraTransakcije();
        SifraTransakcije saved = new SifraTransakcije();
        given(sifraTransakcijeRepository.save(sifraTransakcije)).willReturn(saved);

        assertEquals(saved, sifraTransakcijeService.save(sifraTransakcije));
    }

    @Test
    void findByIdTest() {
        SifraTransakcije sifraTransakcije = new SifraTransakcije();
        given(sifraTransakcijeRepository.findById(1L)).willReturn(Optional.of(sifraTransakcije));

        Optional<SifraTransakcije> result = sifraTransakcijeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(sifraTransakcije, result.get());
    }

    @Test
    void findAllTest() {
        List<SifraTransakcije> sifraTransakcijeList = new ArrayList<>();
        given(sifraTransakcijeRepository.findAll()).willReturn(sifraTransakcijeList);

        assertEquals(sifraTransakcijeList, sifraTransakcijeService.findAll());
    }

    @Test
    void deleteByIdTest() {
        when(sifraTransakcijeRepository.findById(1L)).thenReturn(Optional.of(new SifraTransakcije()));

        sifraTransakcijeService.deleteById(1L);

        then(sifraTransakcijeRepository).should(times(1)).deleteById(1L);
    }
}
