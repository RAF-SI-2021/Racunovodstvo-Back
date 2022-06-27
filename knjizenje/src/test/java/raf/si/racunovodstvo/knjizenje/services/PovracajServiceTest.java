package raf.si.racunovodstvo.knjizenje.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.knjizenje.model.Povracaj;
import raf.si.racunovodstvo.knjizenje.repositories.PovracajRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PovracajServiceTest {

    @InjectMocks
    private PovracajService povracajService;

    @Mock
    private PovracajRepository povracajRepository;

    private static final Long MOCK_ID = 1L;

    @Test
    void testFindAll() {
        List<Povracaj> povracajList = new ArrayList<>();
        given(povracajRepository.findAll()).willReturn(povracajList);

        assertEquals(povracajList, povracajService.findAll());
    }

    @Test
    void testFindById() {
        Povracaj povracaj = new Povracaj();
        given(povracajRepository.findById(MOCK_ID)).willReturn(Optional.of(povracaj));

        assertEquals(povracaj, povracajService.findById(MOCK_ID).get());
    }

    @Test
    void testSave() {
        Povracaj povracaj = new Povracaj();
        given(povracajRepository.save(povracaj)).willReturn(povracaj);

        assertEquals(povracaj, povracajService.save(povracaj));
    }

    @Test
    void testDeleteById() {
        povracajService.deleteById(MOCK_ID);

        then(povracajRepository).should(times(1)).deleteById(MOCK_ID);
    }
}