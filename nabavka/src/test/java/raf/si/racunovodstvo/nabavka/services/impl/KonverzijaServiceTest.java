package raf.si.racunovodstvo.nabavka.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.nabavka.converters.impl.KonverzijaConverter;
import raf.si.racunovodstvo.nabavka.converters.impl.KonverzijaRequestConverter;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.model.Lokacija;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.repositories.KonverzijaRepository;
import raf.si.racunovodstvo.nabavka.repositories.LokacijaRepository;
import raf.si.racunovodstvo.nabavka.requests.KonverzijaRequest;
import raf.si.racunovodstvo.nabavka.requests.LokacijaRequest;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;
import raf.si.racunovodstvo.nabavka.specifications.RacunSpecification;
import raf.si.racunovodstvo.nabavka.specifications.SearchCriteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class KonverzijaServiceTest {

    @InjectMocks
    KonverzijaService konverzijaService;

    @Mock
    KonverzijaRepository konverzijaRepository;

    @Mock
    LokacijaRepository lokacijaRepository;

    @Mock
    KonverzijaConverter konverzijaConverter;

    @Mock
    KonverzijaRequestConverter konverzijaRequestConverter;

    private Lokacija lokacija;

    private Konverzija konverzija;

    private KonverzijaRequest konverzijaRequest;

    private TroskoviNabavke tn1;

    private TroskoviNabavke tn2;

    private TroskoviNabavke tn3;

    private static final Long MOCK_ID = 1L;

    private static final String MOCK_SEARCH_KEY = "MOCK_KEY";
    private static final String MOCK_SEARCH_VALUE = "MOCK_VALUE";
    private static final String MOCK_SEARCH_OPERATION = "MOCK_OPERATION";


    @BeforeEach
    void setUp() {
        tn1 = new TroskoviNabavke();
        tn1.setTroskoviNabavkeId(1L);
        tn1.setCena(1000.0);
        tn1.setNaziv("Trosak1");

        tn2 = new TroskoviNabavke();
        tn2.setTroskoviNabavkeId(2L);
        tn2.setCena(1000.0);
        tn2.setNaziv("Trosak2");

        tn3 = new TroskoviNabavke();
        tn3.setTroskoviNabavkeId(3L);
        tn3.setCena(1000.0);
        tn3.setNaziv("Trosak3");

        lokacija = new Lokacija();

        konverzijaRequest = new KonverzijaRequest();
        konverzijaRequest.setId(1L);
        konverzijaRequest.setBrojKonverzije("1234/22");
        konverzijaRequest.setValuta("EUR");
        konverzijaRequest.setKomentar("Brzo uneti");
        konverzijaRequest.setLokacija(new LokacijaRequest());
        konverzijaRequest.setDatum(new Date());

        konverzija = new Konverzija();
        konverzija.setId(1L);
        konverzija.setBrojKonverzije("1234/22");
        konverzija.setValuta("EUR");
        konverzija.setKomentar("Brzo uneti");
        konverzija.setLokacija(lokacija);
        konverzija.setDatum(new Date());
        konverzija.setFakturnaCena(0.0);
        konverzija.setNabavnaVrednost(0.0);
    }

    @Test
    void deleteById() {
        konverzijaService.deleteById(MOCK_ID);

        then(konverzijaRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void saveKonverzija() {
        KonverzijaResponse konverzijaResponse = new KonverzijaResponse();
        given(konverzijaRepository.save(konverzija)).willReturn(konverzija);
        given(konverzijaConverter.convert(konverzija)).willReturn(konverzijaResponse);
        given(konverzijaRequestConverter.convert(konverzijaRequest)).willReturn(konverzija);

        assertEquals(konverzijaResponse, konverzijaService.saveKonverzija(konverzijaRequest));
    }

    @Test
    void findAll() {
        List<Konverzija> konverzijaList = new ArrayList<>();
        konverzijaList.add(konverzija);
        Pageable pageSort = PageRequest.of(0, 5, Sort.by(Sort.Order.asc("konverzijaId")));
        Specification<Konverzija> specification =
            new RacunSpecification<>(new SearchCriteria(MOCK_SEARCH_KEY, MOCK_SEARCH_VALUE, MOCK_SEARCH_OPERATION));
        KonverzijaResponse konverzijaResponse = new KonverzijaResponse();
        List<Konverzija> konverzijaList1 = konverzijaList.stream().map(konerzija1 -> konverzija).collect(Collectors.toList());
        Page<Konverzija> pageKonverzija = new PageImpl<>(konverzijaList1);
        given(konverzijaRepository.findAll(specification, pageSort)).willReturn(pageKonverzija);
        given(konverzijaConverter.convert(konverzija)).willReturn(konverzijaResponse);

        Page<KonverzijaResponse> result = konverzijaService.findAll(specification, pageSort);

        assertEquals(1, result.getTotalElements());
        assertEquals(konverzijaResponse, result.getContent().get(0));
    }
}
