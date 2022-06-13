package raf.si.racunovodstvo.nabavka.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.nabavka.converters.impl.KalkulacijaConverter;
import raf.si.racunovodstvo.nabavka.converters.impl.KalkulacijaReverseConverter;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.model.KalkulacijaArtikal;
import raf.si.racunovodstvo.nabavka.repositories.KalkulacijaRepository;
import raf.si.racunovodstvo.nabavka.requests.KalkulacijaRequest;
import raf.si.racunovodstvo.nabavka.responses.KalkulacijaResponse;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class KalkulacijaServiceTest {

    @InjectMocks
    private KalkulacijaService kalkulacijaService;

    @Mock
    private KalkulacijaRepository kalkulacijaRepository;

    @Mock
    private KalkulacijaConverter kalkulacijaConverter;

    @Mock
    private KalkulacijaReverseConverter kalkulacijaReverseConverter;

    private static final Long KALKULACIJA_ID_MOCK = 1L;

    @Test
    void saveTest() {
        KalkulacijaRequest kalkulacijaRequest = new KalkulacijaRequest();
        Kalkulacija kalkulacija = new Kalkulacija();
        KalkulacijaResponse kalkulacijaResponse = new KalkulacijaResponse();
        given(kalkulacijaConverter.convert(kalkulacijaRequest)).willReturn(kalkulacija);
        given(kalkulacijaRepository.save(kalkulacija)).willReturn(kalkulacija);
        given(kalkulacijaReverseConverter.convert(kalkulacija)).willReturn(kalkulacijaResponse);

        assertEquals(kalkulacijaResponse, kalkulacijaService.save(kalkulacijaRequest));
    }

    @Test
    void updateTest() {
        KalkulacijaRequest kalkulacijaRequest = new KalkulacijaRequest();
        kalkulacijaRequest.setId(KALKULACIJA_ID_MOCK);
        Kalkulacija kalkulacija = Mockito.mock(Kalkulacija.class);
        KalkulacijaResponse kalkulacijaResponse = Mockito.mock(KalkulacijaResponse.class);
        Optional<Kalkulacija> optionalArtikal = Optional.of(kalkulacija);
        given(kalkulacijaRepository.findById(KALKULACIJA_ID_MOCK)).willReturn(optionalArtikal);
        given(kalkulacijaConverter.convert(kalkulacijaRequest)).willReturn(kalkulacija);
        given(kalkulacijaRepository.save(kalkulacija)).willReturn(kalkulacija);
        given(kalkulacijaReverseConverter.convert(kalkulacija)).willReturn(kalkulacijaResponse);

        assertEquals(kalkulacijaResponse, kalkulacijaService.update(kalkulacijaRequest));
    }

    @Test
    void updateExceptionTest() {
        KalkulacijaRequest kalkulacijaRequest = new KalkulacijaRequest();
        kalkulacijaRequest.setId(KALKULACIJA_ID_MOCK);
        given(kalkulacijaService.findById(KALKULACIJA_ID_MOCK)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> kalkulacijaService.update(kalkulacijaRequest));
    }

    @Test
    void deleteByIdTest() {
        given(kalkulacijaRepository.findById(KALKULACIJA_ID_MOCK)).willReturn(Optional.of(Mockito.mock(Kalkulacija.class)));
        given(kalkulacijaReverseConverter.convert(any())).willReturn(new KalkulacijaResponse());

        kalkulacijaService.deleteById(KALKULACIJA_ID_MOCK);

        then(kalkulacijaRepository).should(times(1)).deleteById(KALKULACIJA_ID_MOCK);
    }

    @Test
    void deleteByIdExceptionTest() {
        given(kalkulacijaRepository.findById(KALKULACIJA_ID_MOCK)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> kalkulacijaService.deleteById(KALKULACIJA_ID_MOCK));
    }

    @Test
    void findAllPageableTest() {
        Kalkulacija kalkulacija = Mockito.mock(Kalkulacija.class);
        Page<Kalkulacija> kalkulacijaPage = new PageImpl<>(List.of(kalkulacija));
        KalkulacijaResponse kalkulacijaResponse = new KalkulacijaResponse();
        Pageable pageable = Mockito.mock(Pageable.class);
        given(kalkulacijaRepository.findAll(pageable)).willReturn(kalkulacijaPage);
        given(kalkulacijaReverseConverter.convert(kalkulacija)).willReturn(kalkulacijaResponse);

        Page<KalkulacijaResponse> result = kalkulacijaService.findAll(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(kalkulacijaResponse, result.getContent().get(0));
    }

    @Test
    void increaseProdajnaAndNabavnaCenaTest() {

        KalkulacijaRequest kalkulacijaRequest = new KalkulacijaRequest();
        kalkulacijaRequest.setId(KALKULACIJA_ID_MOCK);
        Kalkulacija kalkulacija = Mockito.mock(Kalkulacija.class);
        Optional<Kalkulacija> optionalArtikal = Optional.of(kalkulacija);
        given(kalkulacijaRepository.findById(KALKULACIJA_ID_MOCK)).willReturn(optionalArtikal);
        given(kalkulacijaRepository.save(any(Kalkulacija.class))).willReturn(kalkulacija);

        KalkulacijaArtikal kalkulacijaArtikal = new KalkulacijaArtikal();
        kalkulacijaArtikal.setProdajnaCena(1D);
        kalkulacijaArtikal.setUkupnaProdajnaVrednost(1D);
        assertEquals(kalkulacija, kalkulacijaService.increaseNabavnaAndProdajnaCena(KALKULACIJA_ID_MOCK, kalkulacijaArtikal));
    }

    @Test
    void increaseProdajnaAndNabavnaCenaExceptionTest() {
        KalkulacijaRequest kalkulacijaRequest = new KalkulacijaRequest();
        kalkulacijaRequest.setId(KALKULACIJA_ID_MOCK);
        given(kalkulacijaService.findById(KALKULACIJA_ID_MOCK)).willReturn(Optional.empty());
        KalkulacijaArtikal kalkulacijaArtikal = new KalkulacijaArtikal();
        kalkulacijaArtikal.setProdajnaCena(1D);
        kalkulacijaArtikal.setUkupnaProdajnaVrednost(1D);
        assertThrows(EntityNotFoundException.class, () -> kalkulacijaService.increaseNabavnaAndProdajnaCena(KALKULACIJA_ID_MOCK, kalkulacijaArtikal));
    }

    @Test
    void saveKalkulacijaTest() {
        Kalkulacija kalkulacija = new Kalkulacija();
        given(kalkulacijaRepository.save(any(Kalkulacija.class))).willReturn(kalkulacija);

        assertEquals(kalkulacija, kalkulacijaService.save(kalkulacija));
    }
}
