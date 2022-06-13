package raf.si.racunovodstvo.knjizenje.services;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.converters.impl.TransakcijaConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.TransakcijaReverseConverter;
import raf.si.racunovodstvo.knjizenje.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.repositories.TransakcijaRepository;
import raf.si.racunovodstvo.knjizenje.requests.TransakcijaRequest;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TransakcijaServiceTest {

    @InjectMocks
    private TransakcijaService transakcijaService;

    @Mock
    private TransakcijaRepository transakcijaRepository;

    @Mock
    private TransakcijaReverseConverter transakcijaReverseConverter;

    @Mock
    private TransakcijaConverter transakcijaConverter;

    @Mock
    private PreduzeceFeignClient preduzeceFeignClient;

    @Mock
    private Specification<Transakcija> specificationMock;

    private static final String MOCK_TOKEN = "MOCK_TOKEN";
    private static final String MOCK_PREDUZECE_NAZIV = "MOCK_NAZIV";
    private static final Long MOCK_PREDUZECE_ID = 1L;

    @Test
    void findAllTest() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Transakcija transakcija = new Transakcija();
        transakcija.setPreduzeceId(MOCK_PREDUZECE_ID);
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        Preduzece preduzece = new Preduzece();
        preduzece.setNaziv(MOCK_PREDUZECE_NAZIV);
        Page<Transakcija> transakcijaPage = new PageImpl<>(List.of(transakcija));
        given(transakcijaRepository.findAll(pageable)).willReturn(transakcijaPage);
        given(transakcijaReverseConverter.convert(transakcija)).willReturn(transakcijaResponse);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN)).willReturn(ResponseEntity.ok(preduzece));

        Page<TransakcijaResponse> result = transakcijaService.findAll(pageable, MOCK_TOKEN);

        assertEquals(1, result.getTotalElements());
        assertEquals(transakcijaResponse, result.getContent().get(0));
        assertEquals(MOCK_PREDUZECE_NAZIV, transakcijaResponse.getKomitent());
    }

    @Test
    void findAllNullPreduzeceIdTest() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Transakcija transakcija = new Transakcija();
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        Page<Transakcija> transakcijaPage = new PageImpl<>(List.of(transakcija));
        given(transakcijaRepository.findAll(pageable)).willReturn(transakcijaPage);
        given(transakcijaReverseConverter.convert(transakcija)).willReturn(transakcijaResponse);

        Page<TransakcijaResponse> result = transakcijaService.findAll(pageable, MOCK_TOKEN);

        then(preduzeceFeignClient).shouldHaveNoInteractions();
        assertEquals(1, result.getTotalElements());
        assertEquals(transakcijaResponse, result.getContent().get(0));
        assertEquals(Strings.EMPTY, transakcijaResponse.getKomitent());
    }

    @Test
    void searchTest() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Transakcija transakcija = new Transakcija();
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        Page<Transakcija> transakcijaPage = new PageImpl<>(List.of(transakcija));
        given(transakcijaRepository.findAll(specificationMock, pageable)).willReturn(transakcijaPage);
        given(transakcijaReverseConverter.convert(transakcija)).willReturn(transakcijaResponse);

        Page<TransakcijaResponse> result = transakcijaService.search(specificationMock, pageable, MOCK_TOKEN);

        then(preduzeceFeignClient).shouldHaveNoInteractions();
        assertEquals(1, result.getTotalElements());
        assertEquals(transakcijaResponse, result.getContent().get(0));
        assertEquals(Strings.EMPTY, transakcijaResponse.getKomitent());
    }

    @Test
    void saveTest() {
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        TransakcijaRequest transakcijaRequest = new TransakcijaRequest();
        Transakcija transakcija = new Transakcija();
        given(transakcijaConverter.convert(transakcijaRequest)).willReturn(transakcija);
        given(transakcijaRepository.save(transakcija)).willReturn(transakcija);
        given(transakcijaReverseConverter.convert(transakcija)).willReturn(transakcijaResponse);

        assertEquals(transakcijaResponse, transakcijaService.save(transakcijaRequest));
    }

    @Test
    void updateTest() {
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        TransakcijaRequest transakcijaRequest = new TransakcijaRequest();
        transakcijaRequest.setDokumentId(1L);
        Transakcija transakcija = new Transakcija();
        given(transakcijaRepository.findById(transakcijaRequest.getDokumentId())).willReturn(Optional.of(transakcija));
        given(transakcijaConverter.convert(transakcijaRequest)).willReturn(transakcija);
        given(transakcijaRepository.save(transakcija)).willReturn(transakcija);
        given(transakcijaReverseConverter.convert(transakcija)).willReturn(transakcijaResponse);

        assertEquals(transakcijaResponse, transakcijaService.update(transakcijaRequest));
    }

    @Test
    void updateExceptionTest() {
        TransakcijaRequest transakcijaRequest = new TransakcijaRequest();
        transakcijaRequest.setDokumentId(1L);
        given(transakcijaRepository.findById(transakcijaRequest.getDokumentId())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transakcijaService.update(transakcijaRequest));
        then(transakcijaRepository).should(never()).save(any());
    }

    @Test
    void transakcijaSaveTest() {
        Transakcija transakcija = new Transakcija();
        given(transakcijaRepository.save(transakcija)).willReturn(transakcija);

        assertEquals(transakcija, transakcijaService.save(transakcija));
    }

    @Test
    void transakcijaFindByIdTest() {
        Transakcija transakcija = new Transakcija();
        given(transakcijaRepository.findById(1L)).willReturn(Optional.of(transakcija));

        Optional<Transakcija> result = transakcijaService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(transakcija, result.get());
    }

    @Test
    void transakcijaFindAllTest() {
        List<Transakcija> transakcijaList = new ArrayList<>();
        given(transakcijaRepository.findAll()).willReturn(transakcijaList);

        assertEquals(transakcijaList, transakcijaService.findAll());
    }

    @Test
    void deleteByIdTest() {
        Transakcija transakcija = new Transakcija();
        given(transakcijaRepository.findById(1L)).willReturn(Optional.of(transakcija));

        transakcijaService.deleteById(1L);

        then(transakcijaRepository).should(times(1)).deleteById(1L);
    }

    @Test
    void deleteByIdExceptionTest() {
        given(transakcijaRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transakcijaService.deleteById(1L));
        then(transakcijaRepository).should(never()).deleteById(anyLong());
    }
}
