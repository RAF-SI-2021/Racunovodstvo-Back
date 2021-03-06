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
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.Povracaj;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;
import raf.si.racunovodstvo.knjizenje.repositories.PovracajRepository;
import raf.si.racunovodstvo.knjizenje.repositories.SifraTransakcijeRepository;
import raf.si.racunovodstvo.knjizenje.repositories.TransakcijaRepository;
import raf.si.racunovodstvo.knjizenje.requests.ObracunTransakcijeRequest;
import raf.si.racunovodstvo.knjizenje.requests.TransakcijaRequest;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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

    @Mock
    private SifraTransakcijeRepository sifraTransakcijeRepository;

    @Mock
    private FakturaService fakturaService;

    @Mock
    private PovracajRepository povracajRepository;

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

    @Test
    void obracunZaradeTransakcijeTest() {
        List<Transakcija> transakcijeList = mock(List.class);
        ObracunTransakcijeRequest r = new ObracunTransakcijeRequest();
        r.setDatum(new Date());
        r.setPreduzeceId(1L);
        r.setIme("IME");
        r.setSifraTransakcijeId(1L);
        r.setPrezime("PREZIME");
        r.setSifraZaposlenog("SIFRA");
        List<ObracunTransakcijeRequest> list = List.of(r);
        given(transakcijaRepository.saveAll(any(List.class))).willReturn(transakcijeList);
        given(sifraTransakcijeRepository.findById(1L)).willReturn(Optional.of(Mockito.mock(SifraTransakcije.class)));
        assertEquals(transakcijeList, transakcijaService.obracunZaradeTransakcije(list));
    }

    @Test
    void testCreateFromMPFaktura() {
        Faktura mpf1 = new Faktura();
        mpf1.setBrojFakture("MP12/21");
        mpf1.setBrojDokumenta(mpf1.getBrojFakture());
        mpf1.setDatumIzdavanja(getDate(2021, 4, 5));
        mpf1.setRokZaPlacanje(getDate(2021, 4, 5));
        mpf1.setDatumPlacanja(getDate(2021, 4, 5));
        mpf1.setProdajnaVrednost(5300.00);
        mpf1.setRabatProcenat(0.00);
        mpf1.setPorezProcenat(20.00);
        mpf1.setPreduzeceId(1L);
        mpf1.setValuta("RSD");
        mpf1.setTipFakture(TipFakture.MALOPRODAJNA_FAKTURA);
        mpf1.setTipDokumenta(TipDokumenta.FAKTURA);
        mpf1.setKurs(1.00);
        mpf1.setNaplata(0.00);

        when(fakturaService.countMPFakture()).thenReturn(1l);
        TransakcijaResponse transakcijaResponse = transakcijaService.createFromMPFaktura(new Faktura());

        assertNull(transakcijaResponse);
    }

    @Test
    void testCreateFromPovracaj() {
        Povracaj pov1 = new Povracaj();
        pov1.setBrojPovracaja("P01");
        pov1.setDatumPovracaja(getDate(2021, 4, 5));
        pov1.setProdajnaVrednost(5300.00);

        when(povracajRepository.count()).thenReturn(0l);
        TransakcijaResponse transakcijaResponse = transakcijaService.createFromPovracaj(pov1);

        assertNull(transakcijaResponse);
    }

    @Test
    void testFindByPreduzeceId() {
        when(transakcijaRepository.findAllByPreduzeceId(anyLong())).thenReturn(List.of());

        List<TransakcijaResponse> transakcijaResponses = transakcijaService.findByPreduzeceId(1l);

        assertEquals(0, transakcijaResponses.size());
    }

    @Test
    void testFindByPreduzeceIdAndDate() {
        when(transakcijaRepository.findAllByPreduzeceIdAndDatumTransakcijeBetween(anyLong(), any(Date.class), any(Date.class))).thenReturn(List.of());

        List<TransakcijaResponse> transakcijaResponses = transakcijaService.findByPreduzeceIdAndDate(1l, new Date(), new Date());

        assertEquals(0, transakcijaResponses.size());
    }

    private Date getDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
