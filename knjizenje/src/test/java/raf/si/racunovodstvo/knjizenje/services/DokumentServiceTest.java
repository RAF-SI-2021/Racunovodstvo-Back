package raf.si.racunovodstvo.knjizenje.services;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.knjizenje.model.Dokument;
import raf.si.racunovodstvo.knjizenje.repositories.DokumentRepository;


import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DokumentServiceTest {

    @InjectMocks
    private DokumentService dokumentService;

    @Mock
    private DokumentRepository dokumentRepository;

    private static final Long MOCK_ID = 1L;

    @Test
    void testFindAll() {
        List<Dokument> dokumentList = new ArrayList<>();
        given(dokumentRepository.findAll()).willReturn(dokumentList);

        assertEquals(dokumentList, dokumentService.findAll());
    }

    @Test
    void testFindById() {
        Dokument dokument = new Dokument();
        given(dokumentRepository.findById(MOCK_ID)).willReturn(Optional.of(dokument));

        assertEquals(dokument, dokumentService.findById(MOCK_ID).get());
    }

    @Test
    void testSave() {
        Dokument dokument = new Dokument();
        given(dokumentRepository.save(dokument)).willReturn(dokument);

        assertEquals(dokument, dokumentService.save(dokument));
    }

    @Test
    void testDeleteById() {
        dokumentService.deleteById(MOCK_ID);

        then(dokumentRepository).should(times(1)).deleteById(MOCK_ID);
    }
}