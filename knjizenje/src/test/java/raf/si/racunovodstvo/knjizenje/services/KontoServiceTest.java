package raf.si.racunovodstvo.knjizenje.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.repositories.KontoRepository;
import raf.si.racunovodstvo.knjizenje.specifications.RacunSpecification;
import raf.si.racunovodstvo.knjizenje.specifications.SearchCriteria;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class KontoServiceTest {

    @InjectMocks
    private KontoService kontoService;

    @Mock
    private KontoRepository kontoRepository;

    private static final Long MOCK_ID = 1L;
    private static final String MOCK_BROJ_KONTA = "1";
    private static final String MOCK_SEARCH_KEY = "MOCK_KEY";
    private static final String MOCK_SEARCH_VALUE = "MOCK_VALUE";
    private static final String MOCK_SEARCH_OPERATION = "MOCK_OPERATION";

    @Test
    void testFindAll() {
        List<Konto> kontoList = new ArrayList<>();
        given(kontoRepository.findAll()).willReturn(kontoList);

        assertEquals(kontoList, kontoService.findAll());
    }

    @Test
    void testFindAllSpecification() {
        List<Konto> kontoList = new ArrayList<>();
        Specification<Konto> kontoSpecification =
                new RacunSpecification<>(new SearchCriteria(MOCK_SEARCH_KEY, MOCK_SEARCH_OPERATION, MOCK_SEARCH_VALUE));
        given(kontoRepository.findAll(kontoSpecification)).willReturn(kontoList);

        assertEquals(kontoList, kontoService.findAll(kontoSpecification));
    }

    @Test
    void testFindByKontnaGrupa() {
        List<Konto> kontoList = new ArrayList<>();
        given(kontoRepository.findKontoByKontnaGrupaBrojKonta(MOCK_BROJ_KONTA)).willReturn(kontoList);

        assertEquals(kontoList, kontoService.findByKontnaGrupa(MOCK_BROJ_KONTA));
    }

    @Test
    void testSave() {
        Konto konto = new Konto();
        given(kontoRepository.save(konto)).willReturn(konto);

        assertEquals(konto, kontoService.save(konto));
    }

    @Test
    void testFindById() {
        Optional<Konto> optionalKonto = Optional.of(new Konto());
        given(kontoRepository.findById(MOCK_ID)).willReturn(optionalKonto);

        assertEquals(optionalKonto, kontoService.findById(MOCK_ID));
    }

    @Test
    void testDeleteById() {
        kontoService.deleteById(MOCK_ID);

        then(kontoRepository).should(times(1)).deleteById(MOCK_ID);
    }
}