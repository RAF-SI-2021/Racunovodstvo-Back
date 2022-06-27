package raf.si.racunovodstvo.knjizenje.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;
import raf.si.racunovodstvo.knjizenje.converter.impl.KnjizenjeConverter;
import raf.si.racunovodstvo.knjizenje.model.*;
import raf.si.racunovodstvo.knjizenje.repositories.DokumentRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.requests.KnjizenjeRequest;
import raf.si.racunovodstvo.knjizenje.responses.KnjizenjeResponse;
import raf.si.racunovodstvo.knjizenje.specifications.RacunSpecification;
import raf.si.racunovodstvo.knjizenje.specifications.SearchCriteria;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class KnjizenjeServiceTest {

    @InjectMocks
    private KnjizenjeService knjizenjeService;

    @Mock
    private KnjizenjeRepository knjizenjeRepository;
    @Mock
    private DokumentRepository dokumentRepository;
    @Mock
    private KontoService kontoService;
    @Mock
    private KnjizenjeConverter knjizenjeConverter;
    @Mock
    private TroskovniCentarService troskovniCentarService;
    @Mock
    private ProfitniCentarService profitniCentarService;

    private Konto konto1;

    private Konto konto2;

    private Konto konto3;

    private Knjizenje knjizenje;

    private static final Long MOCK_ID = 1L;

    private static final String MOCK_SEARCH_KEY = "MOCK_KEY";
    private static final String MOCK_SEARCH_VALUE = "MOCK_VALUE";
    private static final String MOCK_SEARCH_OPERATION = "MOCK_OPERATION";

    @BeforeEach
    void setUp() {
        konto1 = new Konto();
        konto1.setKontoId(1L);
        konto1.setDuguje(1000.0);
        konto1.setPotrazuje(500.0);

        konto2 = new Konto();
        konto1.setKontoId(2L);
        konto2.setDuguje(2000.0);
        konto2.setPotrazuje(1000.0);

        konto3 = new Konto();
        konto1.setKontoId(3L);
        konto3.setDuguje(0.0);
        konto3.setPotrazuje(1000.0);

        knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(1L);
        knjizenje.setKonto(List.of(konto1, konto2, konto3));

        ReflectionTestUtils.setField(knjizenjeService, "knjizenjeConverter", knjizenjeConverter);
    }

    @Test
    void save() {
        Dokument dokument = new Dokument();
        String brojDokumenta = new String();
        dokument.setBrojDokumenta(brojDokumenta);
        knjizenje.setDokument(dokument);

        given(dokumentRepository.findByBrojDokumenta(knjizenje.getDokument().getBrojDokumenta())).willReturn(Optional.of(dokument));
        given(knjizenjeRepository.save(any(Knjizenje.class))).willReturn(knjizenje);

        assertEquals(knjizenje, knjizenjeService.save(knjizenje));
    }

    @Test
    void saveWithoutDocument() {
        Dokument dokument = new Dokument();
        String brojDokumenta = new String();
        dokument.setBrojDokumenta(brojDokumenta);
        knjizenje.setDokument(dokument);

        given(dokumentRepository.findByBrojDokumenta(knjizenje.getDokument().getBrojDokumenta())).willReturn(Optional.empty());
        given(dokumentRepository.save(dokument)).willReturn(dokument);
        given(knjizenjeRepository.save(any(Knjizenje.class))).willReturn(knjizenje);

        assertEquals(knjizenje, knjizenjeService.save(knjizenje));
    }

    @Test
    void testHappyPath() {
        when(knjizenjeRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(knjizenje));

        double sumaDuguje = knjizenjeService.getSumaDugujeZaKnjizenje(knjizenje.getKnjizenjeId());
        double sumaPotrazuje = knjizenjeService.getSumaPotrazujeZaKnjizenje(knjizenje.getKnjizenjeId());
        double saldo = knjizenjeService.getSaldoZaKnjizenje(knjizenje.getKnjizenjeId());

        assertEquals(3000, sumaDuguje);
        assertEquals(2500, sumaPotrazuje);
        assertEquals(-500, saldo);
    }

    @Test
    void testKnjizenjeNotFound() {
        when(knjizenjeRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(null));
        when(knjizenjeRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(null));

        assertThrows(EntityNotFoundException.class, () -> knjizenjeService.getSumaDugujeZaKnjizenje(2L));
        assertThrows(EntityNotFoundException.class, () -> knjizenjeService.getSumaPotrazujeZaKnjizenje(2L));
    }

    @Test
    void testDugujePotrazujeNotSet() {
        konto1 = new Konto();
        knjizenje.setKonto(List.of(konto1, konto2, konto3));
        when(knjizenjeRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(knjizenje));

        double sumaDuguje = knjizenjeService.getSumaDugujeZaKnjizenje(knjizenje.getKnjizenjeId());
        double sumaPotrazuje = knjizenjeService.getSumaPotrazujeZaKnjizenje(knjizenje.getKnjizenjeId());
        double saldo = knjizenjeService.getSaldoZaKnjizenje(knjizenje.getKnjizenjeId());

        assertEquals(2000, sumaDuguje);
        assertEquals(2000, sumaPotrazuje);
        assertEquals(0, saldo);
    }

    @Test
    void testDeleteById() {
        knjizenjeService.deleteById(MOCK_ID);

        then(knjizenjeRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void testFindAll() {
        List<Knjizenje> knjizenja = new ArrayList<>();
        given(knjizenjeRepository.findAll()).willReturn(knjizenja);

        assertEquals(knjizenja, knjizenjeService.findAll());
    }

    @Test
    void findAllKnjizenjeResponse() {
        List<KnjizenjeResponse> knjizenjeResponseList = new ArrayList<>();
        List<Knjizenje> knjizenja = new ArrayList<>();

        KnjizenjeResponse knjizenjeResponse = new KnjizenjeResponse();
        Page<KnjizenjeResponse> page = new PageImpl<>(knjizenja.stream().map(knjizenje -> knjizenjeResponse)
                                                               .collect(Collectors.toList()));
        given(knjizenjeRepository.findAll()).willReturn(knjizenja);
        given(knjizenjeConverter.convert(knjizenja)).willReturn(page);
        assertEquals(knjizenjeResponseList, knjizenjeService.findAllKnjizenjeResponse());

    }

    @Test
    void findAll() {
        List<Knjizenje> knjizenjeList = new ArrayList<>();
        Knjizenje knjizenje = new Knjizenje();
        knjizenjeList.add(knjizenje);

        Pageable pageSort = PageRequest.of(0, 5, Sort.by(Sort.Order.asc("knjizenjeId")));

        Specification<Knjizenje> specification =
            new RacunSpecification<>(new SearchCriteria(MOCK_SEARCH_KEY, MOCK_SEARCH_VALUE, MOCK_SEARCH_OPERATION));

        KnjizenjeResponse knjizenjeResponse = new KnjizenjeResponse();

        Page<KnjizenjeResponse> page = new PageImpl<>(knjizenjeList.stream().map(knjizenje1 -> knjizenjeResponse)
                                                                   .collect(Collectors.toList()));

        Page<Knjizenje> pageKnjizenje = new PageImpl<>(knjizenjeList.stream().map(knjizenje1 -> knjizenje)
                                                                    .collect(Collectors.toList()));

        lenient().when(knjizenjeRepository.findAll(specification, pageSort)).thenReturn(pageKnjizenje);
        lenient().when(knjizenjeConverter.convert(knjizenjeList)).thenReturn(page);

        assertEquals(page, knjizenjeService.findAll(specification, pageSort));
    }

    @Test
    void saveTest() {
        KnjizenjeRequest kr = Mockito.mock(KnjizenjeRequest.class);
        Knjizenje knj = new Knjizenje();
        Konto k = Mockito.mock(Konto.class);
        Dokument d = Mockito.mock(Dokument.class);
        given(kr.getKonto()).willReturn(List.of(k));
        given(kr.getDokument()).willReturn(d);
        given(d.getBrojDokumenta()).willReturn("BROJ");

        knj.setDatumKnjizenja(kr.getDatumKnjizenja());
        knj.setBrojNaloga(kr.getBrojNaloga());
        knj.setKomentar(kr.getKomentar());
        knj.setDokument(d);

        given(dokumentRepository.findByBrojDokumenta("BROJ")).willReturn(Optional.of(d));
        given(knjizenjeRepository.save(any(Knjizenje.class))).willReturn(knj);
        given(troskovniCentarService.findById(kr.getBazniCentarId())).willReturn(Optional.of(new TroskovniCentar()));
        given(profitniCentarService.findById(kr.getBazniCentarId())).willReturn(Optional.empty());

        assertEquals(knj, knjizenjeService.save(kr));
    }

    @Test
    void saveTest2() {
        KnjizenjeRequest kr = Mockito.mock(KnjizenjeRequest.class);
        Knjizenje knj = new Knjizenje();
        Konto k = Mockito.mock(Konto.class);
        Dokument d = Mockito.mock(Dokument.class);
        given(kr.getKonto()).willReturn(List.of(k));
        given(kr.getDokument()).willReturn(d);
        given(d.getBrojDokumenta()).willReturn("BROJ");

        knj.setDatumKnjizenja(kr.getDatumKnjizenja());
        knj.setBrojNaloga(kr.getBrojNaloga());
        knj.setKomentar(kr.getKomentar());
        knj.setDokument(d);

        given(dokumentRepository.findByBrojDokumenta("BROJ")).willReturn(Optional.of(d));
        given(knjizenjeRepository.save(any(Knjizenje.class))).willReturn(knj);
        given(troskovniCentarService.findById(kr.getBazniCentarId())).willReturn(Optional.empty());
        given(profitniCentarService.findById(kr.getBazniCentarId())).willReturn(Optional.of(new ProfitniCentar()));

        assertEquals(knj, knjizenjeService.save(kr));
    }

    @Test
    void findKontoByKnjizenjeIdNotFoundTest() {
        given(knjizenjeRepository.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> knjizenjeService.findKontoByKnjizenjeId(MOCK_ID));
    }
}
