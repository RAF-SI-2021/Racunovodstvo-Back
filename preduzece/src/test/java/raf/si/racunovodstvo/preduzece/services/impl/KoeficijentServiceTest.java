package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.preduzece.model.Koeficijent;
import raf.si.racunovodstvo.preduzece.repositories.KoeficijentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class KoeficijentServiceTest {

    @InjectMocks
    private KoeficijentService koeficijentService;
    @Mock
    private KoeficijentRepository koeficijentRepository;

    private static final Long MOCK_ID = 1L;

    @Test
    void save() {
        Koeficijent koeficijent = new Koeficijent();
        given(koeficijentRepository.save(koeficijent)).willReturn(koeficijent);

        assertEquals(koeficijent, koeficijentService.save(koeficijent));
    }

    @Test
    void findById() {
        Koeficijent koeficijent = new Koeficijent();
        given(koeficijentRepository.findById(MOCK_ID)).willReturn(Optional.of(koeficijent));

        assertEquals(koeficijent, koeficijentService.findById(MOCK_ID).get());
    }

    @Test
    void findAll() {
        List<Koeficijent> koeficijentList = new ArrayList<>();
        given(koeficijentRepository.findAll()).willReturn(koeficijentList);

        assertEquals(koeficijentList, koeficijentService.findAll());
    }

    @Test
    void deleteById() {
        koeficijentService.deleteById(MOCK_ID);
        then(koeficijentRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void getCurrentKoeficijent() {
        Koeficijent koeficijent = new Koeficijent();
        given(koeficijentRepository.findFirstByOrderByDateDesc()).willReturn(koeficijent);

        assertEquals(koeficijent, koeficijentService.getCurrentKoeficijent());
    }
}
