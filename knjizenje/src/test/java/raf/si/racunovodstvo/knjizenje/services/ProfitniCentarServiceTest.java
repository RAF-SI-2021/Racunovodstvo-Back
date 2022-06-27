package raf.si.racunovodstvo.knjizenje.services;

import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import raf.si.racunovodstvo.knjizenje.converters.impl.BazniKontoConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.ProfitniCentarConverter;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.repositories.BazniKontoRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.repositories.ProfitniCentarRepository;
import raf.si.racunovodstvo.knjizenje.requests.ProfitniCentarRequest;
import raf.si.racunovodstvo.knjizenje.responses.ProfitniCentarResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProfitniCentarServiceTest {

    @InjectMocks
    private ProfitniCentarService profitniCentarService;

    @Mock
    private ProfitniCentarRepository profitniCentarRepository;

    @Mock
    private BazniKontoRepository bazniKontoRepository;

    @Mock
    private KnjizenjeRepository knjizenjeRepository;

    @Mock
    private ProfitniCentarConverter profitniCentarConverter;

    @Mock
    private BazniKontoConverter bazniKontoConverter;

    private static final long MOCK_ID = 1234L;

    @Test
    void testFindBazniKontoById() {
        BazniKonto bazniKonto = new BazniKonto();
        given(bazniKontoRepository.findById(MOCK_ID)).willReturn(Optional.of(bazniKonto));

        assertEquals(bazniKonto, profitniCentarService.findBazniKontoById(MOCK_ID).get());
    }

    @Test
    void deleteBazniKontoById() {
        assertDoesNotThrow(() -> profitniCentarService.deleteBazniKontoById(MOCK_ID));
    }

    @Test
    void findAllTest() {
        assertDoesNotThrow(() -> profitniCentarService.findAll());
    }

    @Test
    void testSave() {
        ProfitniCentar profitniCentar = new ProfitniCentar();
        given(profitniCentarRepository.save(profitniCentar)).willReturn(profitniCentar);

        ProfitniCentar result = profitniCentarService.save(profitniCentar);

        assertEquals(profitniCentar, result);
    }

    @Test
    void testFindById() {
        assertDoesNotThrow(() -> profitniCentarService.findById(MOCK_ID));
    }

    @Test
    void testFindAll() {
        List<ProfitniCentar> profitniCentarList = new ArrayList<>();
        given(profitniCentarRepository.findAll()).willReturn(profitniCentarList);

        List<ProfitniCentar> result = profitniCentarService.findAll();

        assertEquals(profitniCentarList, result);
    }

    @Test
    void findAllProfitniCentarResponseTest() {
        List<ProfitniCentar> list = Mockito.mock(List.class);
        List<ProfitniCentarResponse> response = List.of(Mockito.mock(ProfitniCentarResponse.class));
        Page<ProfitniCentarResponse> page = new PageImpl<>(response);
        given(profitniCentarRepository.findAll()).willReturn(list);
        given(profitniCentarConverter.convert(list)).willReturn(page);
        assertEquals(response, profitniCentarService.findAllProfitniCentarResponse());
    }

    @Test
    void testFindAllSort() {
        List<ProfitniCentar> profitniCentarList = new ArrayList<>();
        Pageable sort = Mockito.mock(Pageable.class);
        given(profitniCentarRepository.findAll(sort)).willReturn(new PageImpl<>(profitniCentarList));

        Page<ProfitniCentar> result = profitniCentarService.findAll(sort);

        assertEquals(profitniCentarList, result.getContent());
    }

    @Test
    void testDeleteById() {
        profitniCentarService.deleteById(MOCK_ID);

        then(profitniCentarRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void addKontosFromKnjizenjeTest() {
        Knjizenje k = new Knjizenje();
        ProfitniCentar p = new ProfitniCentar();
        ProfitniCentar p2 = new ProfitniCentar();
        Konto kon = new Konto();
        BazniKonto bk = new BazniKonto();
        BazniKonto bk2 = new BazniKonto();
        k.setKnjizenjeId(MOCK_ID);
        kon.setDuguje(1.0);
        kon.setPotrazuje(1.0);
        bk2.setDuguje(1.0);
        bk2.setPotrazuje(1.0);
        p.setKontoList(new ArrayList<>());
        p.setUkupniTrosak(1.0);
        p.setParentProfitniCentar(p2);
        p2.setKontoList(List.of(bk2));
        k.setKonto(List.of(kon));
        given(bazniKontoConverter.convert(kon)).willReturn(bk);
        given(knjizenjeRepository.findById(MOCK_ID)).willReturn(Optional.of(k));
        given(profitniCentarRepository.save(p)).willReturn(p);
        given(profitniCentarRepository.save(p2)).willReturn(p2);
        assertEquals(p, profitniCentarService.addKontosFromKnjizenje(k, p));
    }

    @Test
    void updateProfitniCentarTest() {
        Knjizenje k = new Knjizenje();
        ProfitniCentar p = new ProfitniCentar();
        ProfitniCentar p2 = new ProfitniCentar();
        ProfitniCentarRequest req = new ProfitniCentarRequest();
        req.setId(MOCK_ID);
        Konto kon = new Konto();
        BazniKonto bk = new BazniKonto();
        BazniKonto bk2 = new BazniKonto();
        k.setKnjizenjeId(MOCK_ID);
        p.setUkupniTrosak(1.0);
        p2.setUkupniTrosak(1.0);
        p.setKontoList(List.of(bk));
        kon.setDuguje(1.0);
        kon.setPotrazuje(1.0);
        bk2.setDuguje(1.0);
        bk2.setPotrazuje(1.0);
        req.setKontoList(List.of(bk2));
        req.setUkupniTrosak(1.0);
        req.setParentProfitniCentar(p);
        req.setProfitniCentarList(List.of(p2));
        k.setKonto(List.of(kon));
        given(profitniCentarRepository.findById(MOCK_ID)).willReturn(Optional.of(p));
        given(profitniCentarRepository.save(p)).willReturn(p);
        assertEquals(p, profitniCentarService.updateProfitniCentar(req));
    }
}