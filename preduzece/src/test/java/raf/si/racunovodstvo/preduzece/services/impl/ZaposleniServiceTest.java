package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.preduzece.model.Preduzece;
import raf.si.racunovodstvo.preduzece.model.Staz;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;
import raf.si.racunovodstvo.preduzece.repositories.ZaposleniRepository;
import raf.si.racunovodstvo.preduzece.responses.ZaposleniResponse;
import raf.si.racunovodstvo.preduzece.specifications.RacunSpecification;
import raf.si.racunovodstvo.preduzece.specifications.SearchCriteria;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZaposleniServiceTest {

    @InjectMocks
    private ZaposleniService zaposleniService;
    @Mock
    private ZaposleniRepository zaposleniRepository;
    @Mock
    private StazService stazService;
    @Mock
    private ModelMapper modelMapper;

    private static final Long MOCK_ID = 1L;

    private static final String MOCK_SEARCH_KEY = "MOCK_KEY";
    private static final String MOCK_SEARCH_VALUE = "MOCK_VALUE";
    private static final String MOCK_SEARCH_OPERATION = "MOCK_OPERATION";

    @Test
    void save() {
        Zaposleni zaposleni = new Zaposleni();
        given(stazService.save(any(Staz.class))).willReturn(new Staz());
        given(zaposleniRepository.save(zaposleni)).willReturn(zaposleni);

        assertEquals(zaposleni, zaposleniService.save(zaposleni));
    }

    @Test
    void findById() {
        Zaposleni zaposleni = new Zaposleni();
        given(zaposleniRepository.findById(MOCK_ID)).willReturn(Optional.of(zaposleni));

        assertEquals(zaposleni, zaposleniService.findById(MOCK_ID).get());
    }

    @Test
    void findAll() {
        List<Zaposleni> zaposleniList = new ArrayList<>();
        given(zaposleniRepository.findAll()).willReturn(zaposleniList);

        assertEquals(zaposleniList, zaposleniService.findAll());
    }

    @Test
    void deleteById() {
        zaposleniService.deleteById(MOCK_ID);
        then(zaposleniRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void testFindAll() {
        Zaposleni zaposleni = new Zaposleni();
        ZaposleniResponse zaposleniResponse = new ZaposleniResponse();
        zaposleni.setZaposleniId(MOCK_ID);
        List<Zaposleni> zaposleniList = new ArrayList<>(List.of(zaposleni));
        Specification<Zaposleni> specification =
                new RacunSpecification<>(new SearchCriteria(MOCK_SEARCH_KEY, MOCK_SEARCH_VALUE, MOCK_SEARCH_OPERATION));

        given(zaposleniRepository.findAll(specification)).willReturn(zaposleniList);
        given(modelMapper.map(zaposleni, ZaposleniResponse.class)).willReturn(zaposleniResponse);

        assertTrue(zaposleniService.findAll(specification).contains(zaposleniResponse));
    }

    @Test
    void otkazZaposleni() {
        Zaposleni zaposleni = new Zaposleni();
        Staz staz = new Staz();
        staz.setKrajRada(null);
        List<Staz> stazList = new ArrayList<>();
        stazList.add(staz);
        zaposleni.setZaposleniId(MOCK_ID);

        ZaposleniResponse expectedResponse = new ZaposleniResponse();
        expectedResponse.setZaposleniId(MOCK_ID);
        expectedResponse.setStatusZaposlenog(StatusZaposlenog.NEZAPOSLEN);

        zaposleni.setStaz(stazList);
        given(zaposleniRepository.findById(MOCK_ID)).willReturn(Optional.of(zaposleni));
        given(stazService.save(staz)).willReturn(staz);
        given(zaposleniRepository.save(zaposleni)).willReturn(zaposleni);
        given(modelMapper.map(zaposleni, ZaposleniResponse.class)).willReturn(expectedResponse);

        ZaposleniResponse actualResponse = zaposleniService.otkazZaposleni(zaposleni);

        assertEquals(expectedResponse.getZaposleniId(), actualResponse.getZaposleniId());
        assertEquals(expectedResponse.getStatusZaposlenog(), actualResponse.getStatusZaposlenog());
    }

    @Test
    void otkazZaposleniStatusNezaposlen() {
        Zaposleni zaposleni = new Zaposleni();
        Staz staz = new Staz();
        staz.setKrajRada(new Date());
        List<Staz> stazList = new ArrayList<>();
        stazList.add(staz);
        zaposleni.setZaposleniId(MOCK_ID);

        zaposleni.setStaz(stazList);
        given(zaposleniRepository.findById(MOCK_ID)).willReturn(Optional.of(zaposleni));

        assertThrows(OperationNotSupportedException.class, () -> zaposleniService.otkazZaposleni(zaposleni));
    }

    @Test
    void updateZaposleniException1() {
        Zaposleni zaposleni = new Zaposleni();
        zaposleni.setZaposleniId(MOCK_ID);
        given(zaposleniRepository.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> zaposleniService.updateZaposleni(zaposleni));
    }

    @Test
    void updateZaposleniException2() {
        Zaposleni zaposlen = new Zaposleni();
        zaposlen.setZaposleniId(MOCK_ID);
        zaposlen.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);

        Zaposleni nezaposlen = new Zaposleni();
        nezaposlen.setZaposleniId(MOCK_ID);
        nezaposlen.setStatusZaposlenog(StatusZaposlenog.NEZAPOSLEN);

        given(zaposleniRepository.findById(MOCK_ID)).willReturn(Optional.of(zaposlen));

        assertThrows(OperationNotSupportedException.class, () -> zaposleniService.updateZaposleni(nezaposlen));
    }

    @Test
    void updateZaposleni() {
        Zaposleni zaposlen = new Zaposleni();
        zaposlen.setZaposleniId(MOCK_ID);
        zaposlen.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        Zaposleni nezaposlen = new Zaposleni();
        nezaposlen.setZaposleniId(MOCK_ID);
        nezaposlen.setStaz(new ArrayList<>());
        nezaposlen.setStatusZaposlenog(StatusZaposlenog.NEZAPOSLEN);
        ZaposleniResponse expectedResponse = new ZaposleniResponse();
        expectedResponse.setZaposleniId(MOCK_ID);
        expectedResponse.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        given(zaposleniRepository.findById(MOCK_ID)).willReturn(Optional.of(nezaposlen));
        given(stazService.save(any(Staz.class))).willReturn(new Staz());
        given(zaposleniRepository.save(zaposlen)).willReturn(zaposlen);
        given(modelMapper.map(zaposlen, ZaposleniResponse.class)).willReturn(expectedResponse);

        ZaposleniResponse actualResponse = zaposleniService.updateZaposleni(zaposlen);

        assertEquals(expectedResponse.getZaposleniId(), actualResponse.getZaposleniId());
        assertEquals(expectedResponse.getStatusZaposlenog(), actualResponse.getStatusZaposlenog());
    }

    @Test
    void updateZaposleni1() {
        Zaposleni zaposlen = new Zaposleni();
        zaposlen.setZaposleniId(MOCK_ID);
        zaposlen.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);

        Zaposleni zaposlen2 = new Zaposleni();
        zaposlen2.setZaposleniId(MOCK_ID);
        zaposlen2.setStaz(new ArrayList<>());
        zaposlen2.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);

        ZaposleniResponse expectedResponse = new ZaposleniResponse();
        expectedResponse.setZaposleniId(MOCK_ID);
        expectedResponse.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);

        lenient().when(zaposleniRepository.findById(MOCK_ID)).thenReturn(Optional.of(zaposlen2));
        lenient().when(stazService.save(any(Staz.class))).thenReturn(new Staz());
        lenient().when(zaposleniRepository.save(zaposlen)).thenReturn(zaposlen);
        given(modelMapper.map(zaposlen, ZaposleniResponse.class)).willReturn(expectedResponse);

        ZaposleniResponse actualResponse = zaposleniService.updateZaposleni(zaposlen);

        assertEquals(expectedResponse.getZaposleniId(), actualResponse.getZaposleniId());
        assertEquals(expectedResponse.getStatusZaposlenog(), actualResponse.getStatusZaposlenog());
    }

    @Test
    void saveZaposleniTest(){
        ZaposleniResponse response = new ZaposleniResponse();
        Zaposleni zaposleni = new Zaposleni();

        given(stazService.save(any(Staz.class))).willReturn(new Staz());
        given(zaposleniRepository.save(zaposleni)).willReturn(zaposleni);
        given(modelMapper.map(zaposleni, ZaposleniResponse.class)).willReturn(response);

        assertEquals(response, zaposleniService.saveZaposleni(zaposleni));
    }

    @Test
    void findZaposleniById(){
        ZaposleniResponse response = new ZaposleniResponse();
        Zaposleni zaposleni = new Zaposleni();

        given(zaposleniRepository.findById(MOCK_ID)).willReturn(Optional.of(zaposleni));
        given(modelMapper.map(zaposleni, ZaposleniResponse.class)).willReturn(response);

        assertEquals(response, zaposleniService.findZaposleniById(MOCK_ID).get());
    }
}
