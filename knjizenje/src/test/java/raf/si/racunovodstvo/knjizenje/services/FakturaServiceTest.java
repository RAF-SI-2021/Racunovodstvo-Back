package raf.si.racunovodstvo.knjizenje.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;
import raf.si.racunovodstvo.knjizenje.repositories.FakturaRepository;
import raf.si.racunovodstvo.knjizenje.specifications.RacunSpecification;
import raf.si.racunovodstvo.knjizenje.specifications.SearchCriteria;
import raf.si.racunovodstvo.knjizenje.utils.Utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FakturaServiceTest {

    @InjectMocks
    private FakturaService fakturaService;

    @Mock
    private FakturaRepository fakturaRepository;

    private List<Faktura> fakture;

    private static final String MOCK_ULAZNA_FAKTURA = "ULAZNA_FAKTURA";
    private static final String MOCK_SUMA_POREZ = "sumaPorez";
    private static final String MOCK_SUMA_PRODAJNA_VREDNOST = "sumaProdajnaVrednost";
    private static final String MOCK_SUMA_RABAT = "sumaRabat";
    private static final String MOCK_SUMA_ZA_NAPLATU = "sumaZaNaplatu";
    private static final String MOCK_SEARCH_KEY = "MOCK_KEY";
    private static final String MOCK_SEARCH_VALUE = "MOCK_VALUE";
    private static final String MOCK_SEARCH_OPERATION = "MOCK_OPERATION";
    private static final Long MOCK_DOCUMENT_ID = 1L;

    @BeforeEach
    public void setup() {
        fakture = new ArrayList<>();
    }

    @Test
    void testAllFakture() {
        Faktura f1 = new Faktura();
        f1.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        Faktura f2 = new Faktura();
        f2.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        Faktura f3 = new Faktura();
        f3.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        Faktura f4 = new Faktura();
        f4.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fakture.add(f1);
        fakture.add(f2);
        fakture.add(f3);
        fakture.add(f4);
        when(fakturaRepository.findAll()).thenReturn(fakture);
        List<Faktura> rezultat = fakturaService.findAll();
        assertEquals(4, rezultat.size());
    }

    @Test
    void testFindAllSpecification() {
        Specification<Faktura> specification =
                new RacunSpecification<>(new SearchCriteria(MOCK_SEARCH_KEY, MOCK_SEARCH_VALUE, MOCK_SEARCH_OPERATION));
        given(fakturaRepository.findAll(specification)).willReturn(fakture);

        assertEquals(fakture, fakturaService.findAll(specification));
    }

    @Test
    void testFindAllSort() {
        Pageable sort = Mockito.mock(Pageable.class);
        given(fakturaRepository.findAll(sort)).willReturn(new PageImpl<>(fakture));

        assertEquals(fakture, fakturaService.findAll(sort).getContent());
    }

    @Test
    void testFindById() {
        Faktura faktura = new Faktura();
        given(fakturaRepository.findByDokumentId(MOCK_DOCUMENT_ID)).willReturn(Optional.of(faktura));

        assertEquals(faktura, fakturaService.findById(MOCK_DOCUMENT_ID).get());
    }

    @Test
    void testSave() {
        Faktura ocekivanaFaktura = new Faktura();

        ocekivanaFaktura.setProdajnaVrednost(123.1);
        ocekivanaFaktura.setRabatProcenat(123.1);
        ocekivanaFaktura.setPorezProcenat(123.1);
        ocekivanaFaktura.setRabat(123.1);
        ocekivanaFaktura.setPorez(123.1);
        ocekivanaFaktura.setIznos(123.1);

        when(fakturaRepository.save(ocekivanaFaktura)).thenReturn(ocekivanaFaktura);
        Faktura vracenaFaktura = fakturaService.save(ocekivanaFaktura);

        assertSame(ocekivanaFaktura, vracenaFaktura);
    }

    @Test
    void testGetSume() {
        List<Double> porezList = new ArrayList<>(Arrays.asList(1.0, 2.0));
        List<Double> rabatList = new ArrayList<>(Arrays.asList(2.0, 3.0));
        List<Double> naplataList = new ArrayList<>(Arrays.asList(3.0, 4.0));
        List<Double> prodajnaVrednostList = new ArrayList<>(Arrays.asList(4.0, 5.0));
        TipFakture tip = TipFakture.valueOf(MOCK_ULAZNA_FAKTURA);
        given(fakturaRepository.findPorezForTipFakture(tip)).willReturn(porezList);
        given(fakturaRepository.findNaplataForTipFakture(tip)).willReturn(naplataList);
        given(fakturaRepository.findProdajnaVrednostForTipFakture(tip)).willReturn(prodajnaVrednostList);
        given(fakturaRepository.findRabatForTipFakture(tip)).willReturn(rabatList);

        Map<String, Double> result = fakturaService.getSume(MOCK_ULAZNA_FAKTURA);

        assertEquals(Utils.sum(porezList), result.get(MOCK_SUMA_POREZ));
        assertEquals(Utils.sum(naplataList), result.get(MOCK_SUMA_ZA_NAPLATU));
        assertEquals(Utils.sum(rabatList), result.get(MOCK_SUMA_RABAT));
        assertEquals(Utils.sum(prodajnaVrednostList), result.get(MOCK_SUMA_PRODAJNA_VREDNOST));
    }

    @Test
    void testDeleteById() {
        fakturaService.deleteById(MOCK_DOCUMENT_ID);

        then(fakturaRepository).should(times(1)).deleteById(MOCK_DOCUMENT_ID);
    }
}

