package raf.si.racunovodstvo.knjizenje.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.knjizenje.converters.impl.BazniKontoConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.TroskovniCentarConverter;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;
import raf.si.racunovodstvo.knjizenje.repositories.BazniKontoRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.repositories.TroskovniCentarRepository;
import raf.si.racunovodstvo.knjizenje.requests.TroskovniCentarRequest;
import raf.si.racunovodstvo.knjizenje.responses.TroskovniCentarResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TroskovniCentarServiceTest {

    @InjectMocks
    private TroskovniCentarService troskovniCentarService;

    @Mock
    private TroskovniCentarRepository troskovniCentarRepository;

    @Mock
    private BazniKontoRepository bazniKontoRepository;

    @Mock
    private KnjizenjeRepository knjizenjeRepository;

    @Mock
    private TroskovniCentarConverter troskovniCentarConverter;

    @Mock
    private BazniKontoConverter bazniKontoConverter;

    private static final long MOCK_ID = 1234L;

    @Test
    void testFindBazniKontoById() {
        BazniKonto bazniKonto = new BazniKonto();
        given(bazniKontoRepository.findById(MOCK_ID)).willReturn(Optional.of(bazniKonto));

        assertEquals(bazniKonto, troskovniCentarService.findBazniKontoById(MOCK_ID).get());
    }

    @Test
    void deleteBazniKontoById() {
        assertDoesNotThrow(() -> troskovniCentarService.deleteBazniKontoById(MOCK_ID));
    }

    @Test
    void findAllTest() {
        assertDoesNotThrow(() -> troskovniCentarService.findAll());
    }

    @Test
    void testSave() {
        TroskovniCentar troskovniCentar = new TroskovniCentar();
        given(troskovniCentarRepository.save(troskovniCentar)).willReturn(troskovniCentar);

        TroskovniCentar result = troskovniCentarService.save(troskovniCentar);

        assertEquals(troskovniCentar, result);
    }

    @Test
    void testFindById() {
        assertDoesNotThrow(() -> troskovniCentarService.findById(MOCK_ID));
    }

    @Test
    void testFindAll() {
        List<TroskovniCentar> troskovniCentarList = new ArrayList<>();
        given(troskovniCentarRepository.findAll()).willReturn(troskovniCentarList);

        List<TroskovniCentar> result = troskovniCentarService.findAll();

        assertEquals(troskovniCentarList, result);
    }

    @Test
    void findAllTroskovniCentarResponseTest() {
        List<TroskovniCentar> list = Mockito.mock(List.class);
        List<TroskovniCentarResponse> response = List.of(Mockito.mock(TroskovniCentarResponse.class));
        Page<TroskovniCentarResponse> page = new PageImpl<>(response);
        given(troskovniCentarRepository.findAll()).willReturn(list);
        given(troskovniCentarConverter.convert(list)).willReturn(page);
        assertEquals(response, troskovniCentarService.findAllTroskovniCentriResponse());
    }

    @Test
    void testFindAllSort() {
        List<TroskovniCentar> troskovniCentarList = new ArrayList<>();
        Pageable sort = Mockito.mock(Pageable.class);
        given(troskovniCentarRepository.findAll(sort)).willReturn(new PageImpl<>(troskovniCentarList));

        Page<TroskovniCentar> result = troskovniCentarService.findAll(sort);

        assertEquals(troskovniCentarList, result.getContent());
    }

    @Test
    void testDeleteById() {
        troskovniCentarService.deleteById(MOCK_ID);

        then(troskovniCentarRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void addKontosFromKnjizenjeTest() {
        Knjizenje k = new Knjizenje();
        TroskovniCentar p = new TroskovniCentar();
        TroskovniCentar p2 = new TroskovniCentar();
        Konto kon = new Konto();
        BazniKonto bk = new BazniKonto();
        BazniKonto bk2 = new BazniKonto();
        k.setKnjizenjeId(MOCK_ID);
        kon.setDuguje(1.0);
        kon.setPotrazuje(1.0);
        bk.setDuguje(1.0);
        bk.setPotrazuje(1.0);
        bk2.setDuguje(1.0);
        bk2.setPotrazuje(1.0);
        p.setKontoList(new ArrayList<>());
        p.setUkupniTrosak(1.0);
        p.setParentTroskovniCentar(p2);
        p2.setKontoList(List.of(bk2));
        k.setKonto(List.of(kon));
        given(bazniKontoConverter.convert(kon)).willReturn(bk);
        given(knjizenjeRepository.findById(MOCK_ID)).willReturn(Optional.of(k));
        given(troskovniCentarRepository.save(p)).willReturn(p);
        given(troskovniCentarRepository.save(p2)).willReturn(p2);
        assertEquals(p, troskovniCentarService.addKontosFromKnjizenje(k, p));
    }

    @Test
    void updateTroskovniCentarTest() {
        Knjizenje k = new Knjizenje();
        TroskovniCentar p = new TroskovniCentar();
        TroskovniCentar p2 = new TroskovniCentar();
        TroskovniCentarRequest req = new TroskovniCentarRequest();
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
        req.setParentTroskovniCentar(p);
        req.setTroskovniCentarList(List.of(p2));
        k.setKonto(List.of(kon));
        given(troskovniCentarRepository.findById(MOCK_ID)).willReturn(Optional.of(p));
        given(troskovniCentarRepository.save(p)).willReturn(p);
        assertEquals(p, troskovniCentarService.updateTroskovniCentar(req));
    }
}