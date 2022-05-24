package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.preduzece.model.Koeficijent;
import raf.si.racunovodstvo.preduzece.model.Staz;
import raf.si.racunovodstvo.preduzece.repositories.StazRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StazServiceTest {

    @InjectMocks
    private StazService stazService;
    @Mock
    private StazRepository stazRepository;

    private static final Long MOCK_ID = 1L;


    @Test
    void save() {
        Staz staz = new Staz();
        given(stazRepository.save(staz)).willReturn(staz);

        assertEquals(staz, stazService.save(staz));
    }

    @Test
    void findById() {
        Staz staz = new Staz();
        given(stazRepository.findById(MOCK_ID)).willReturn(Optional.of(staz));

        assertEquals(staz, stazService.findById(MOCK_ID).get());
    }

    @Test
    void findAll() {
        List<Staz> stazList = new ArrayList<>();
        given(stazRepository.findAll()).willReturn(stazList);

        assertEquals(stazList, stazService.findAll());
    }

    @Test
    void deleteById() {
        stazService.deleteById(MOCK_ID);
        then(stazRepository).should(times(1)).deleteById(MOCK_ID);
    }
}