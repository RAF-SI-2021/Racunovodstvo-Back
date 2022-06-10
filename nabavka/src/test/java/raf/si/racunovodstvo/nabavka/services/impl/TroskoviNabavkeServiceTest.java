package raf.si.racunovodstvo.nabavka.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.repositories.TroskoviNabavkeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TroskoviNabavkeServiceTest {

    @InjectMocks
    private TroskoviNabavkeService troskoviNabavkeService;
    @Mock
    private TroskoviNabavkeRepository troskoviNabavkeRepository;

    private static final Long MOCK_ID = 1L;

    @Test
    void saveTest() {
        TroskoviNabavke troskoviNabavke = new TroskoviNabavke();
        given(troskoviNabavkeRepository.save(troskoviNabavke)).willReturn(troskoviNabavke);

        assertEquals(troskoviNabavke, troskoviNabavkeService.save(troskoviNabavke));
    }

    @Test
    void findByIdTest() {
        TroskoviNabavke troskoviNabavke = new TroskoviNabavke();
        given(troskoviNabavkeRepository.findById(MOCK_ID)).willReturn(Optional.of(troskoviNabavke));

        assertEquals(troskoviNabavke, troskoviNabavkeService.findById(MOCK_ID).get());
    }

    @Test
    void findAllTest() {
        List<TroskoviNabavke> troskoviNabavkeList = new ArrayList<>();
        given(troskoviNabavkeRepository.findAll()).willReturn(troskoviNabavkeList);

        assertEquals(troskoviNabavkeList, troskoviNabavkeService.findAll());
    }

    @Test
    void deleteByIdTest() {
        given(troskoviNabavkeRepository.findById(MOCK_ID)).willReturn(Optional.of(new TroskoviNabavke()));

        troskoviNabavkeService.deleteById(MOCK_ID);

        then(troskoviNabavkeRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void deleteByIdExceptionTest() {
        given(troskoviNabavkeRepository.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> troskoviNabavkeService.deleteById(MOCK_ID));
        then(troskoviNabavkeRepository).should(never()).deleteById(MOCK_ID);
    }
}
