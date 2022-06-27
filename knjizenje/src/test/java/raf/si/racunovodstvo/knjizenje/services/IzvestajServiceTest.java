package raf.si.racunovodstvo.knjizenje.services;

import com.itextpdf.text.DocumentException;
import com.sun.xml.bind.v2.TODO;
import org.junit.jupiter.api.BeforeEach;
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
import raf.si.racunovodstvo.knjizenje.converters.impl.BilansSchemaConverter;
import raf.si.racunovodstvo.knjizenje.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.knjizenje.feign.UserFeignClient;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;
import raf.si.racunovodstvo.knjizenje.reports.BilansTableContent;
import raf.si.racunovodstvo.knjizenje.reports.Report;
import raf.si.racunovodstvo.knjizenje.reports.Reports;
import raf.si.racunovodstvo.knjizenje.reports.TableReport;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;
import raf.si.racunovodstvo.knjizenje.responses.UserResponse;
import raf.si.racunovodstvo.knjizenje.services.helpers.SifraTransakcijaHelper;
import raf.si.racunovodstvo.knjizenje.services.helpers.StatickiIzvestajOTransakcijamaHelper;
import raf.si.racunovodstvo.knjizenje.services.impl.IBilansService;
import raf.si.racunovodstvo.knjizenje.services.impl.ISifraTransakcijeService;
import raf.si.racunovodstvo.knjizenje.services.impl.ITransakcijaService;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IzvestajServiceTest {

    @InjectMocks
    private IzvestajService izvestajService;

    @Mock
    private IBilansService bilansService;

    @Mock
    private PreduzeceFeignClient preduzeceFeignClient;

    @Mock
    private UserFeignClient userFeignClient;

    @Mock
    private ITransakcijaService transakcijaService;

    @Mock
    private SifraTransakcijeService sifraTransakcijeService;

    @Mock
    private SearchUtil<SifraTransakcije> searchUtil;

    private UserResponse userResponse;
    private List<BilansResponse> bilansResponseList;

    private Map<String, List<BilansResponse>> bilansResponseListMap;

    private static final String MOCK_NAME = "MOCK_NAME";
    private static final String MOCK_TITLE = "MOCK_TITLE";
    private static final String MOCK_BROJ_KONTA_OD = "MOCK_OD";
    private static final String MOCK_BROJ_KONTA_DO = "MOCK_DO";
    private static final String MOCK_BROJ_KONTA = "MOCK_BROJ";
    private static final String MOCK_NAZIV = "MOCK_NAZIV";
    private static final String MOCK_TOKEN = "MOCK_TOKEN";
    private static final String MOCK_ADRESA = "MOCK_ADRESA";
    private static final String MOCK_GRAD = "MOCK_GRAD";
    private static final Long MOCK_PREDUZECE_ID = 1L;
    private static final Long MOCK_BROJ_STAVKI = 3L;
    private static final Double MOCK_DUGUJE = 2.0;
    private static final Double MOCK_POTRAZUJE = 1.0;
    private static final Date MOCK_DATUM_OD = new Date();
    private static final Date MOCK_DATUM_DO = new Date();

    @BeforeEach
    void setup() {
        BilansResponse bilansResponse = new BilansResponse(MOCK_DUGUJE, MOCK_POTRAZUJE, MOCK_BROJ_STAVKI, MOCK_BROJ_KONTA, MOCK_NAZIV);

        bilansResponseListMap = Map.of("", List.of(bilansResponse));
        bilansResponseList = List.of(bilansResponse);
        userResponse = new UserResponse();
        userResponse.setUsername(MOCK_NAME);
    }

    @Test
    void makeBrutoBilansTableReportTest() {
        given(bilansService.findBrutoBilans(MOCK_BROJ_KONTA_OD, MOCK_BROJ_KONTA_DO, MOCK_DATUM_OD, MOCK_DATUM_DO)).willReturn(
            bilansResponseList);
        given(userFeignClient.getCurrentUser(MOCK_TOKEN)).willReturn(ResponseEntity.ok(userResponse));
        TableReport result = (TableReport) izvestajService.makeBrutoBilansTableReport(MOCK_TOKEN,
                                                                                      MOCK_TITLE,
                                                                                      MOCK_DATUM_OD,
                                                                                      MOCK_DATUM_DO,
                                                                                      MOCK_BROJ_KONTA_OD,
                                                                                      MOCK_BROJ_KONTA_DO);
        assertEquals(MOCK_NAME, result.getAuthor());
        assertEquals(MOCK_TITLE, result.getTitle());
        assertEquals(BilansTableContent.BILANS_COLUMNS_SINGLE_PERIOD, result.getColumns());
    }

    @Test
    void makeBilansTableReportTest() {
        Preduzece preduzece = new Preduzece();
        preduzece.setAdresa(MOCK_ADRESA);
        preduzece.setGrad(MOCK_GRAD);
        preduzece.setNaziv(MOCK_NAZIV);
        given(userFeignClient.getCurrentUser(MOCK_TOKEN)).willReturn(ResponseEntity.ok(userResponse));
        given(bilansService.findBilans(eq(true), anyList(), anyList())).willReturn(bilansResponseListMap);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN)).willReturn(ResponseEntity.ok(preduzece));

        TableReport result = (TableReport) izvestajService.makeBilansTableReport(MOCK_PREDUZECE_ID,
                                                                                 MOCK_TOKEN,
                                                                                 MOCK_TITLE,
                                                                                 List.of(MOCK_DATUM_OD),
                                                                                 List.of(MOCK_DATUM_DO),
                                                                                 false);
        assertEquals(MOCK_NAME, result.getAuthor());
        assertEquals(MOCK_TITLE, result.getTitle());
        assertEquals(BilansTableContent.BILANS_COLUMNS_SINGLE_PERIOD, result.getColumns());
        assertTrue(result.getFooter().contains(MOCK_ADRESA));
        assertTrue(result.getFooter().contains(MOCK_GRAD));
        assertTrue(result.getFooter().contains(MOCK_NAZIV));
    }

    @Test
    void makeBilansTableReportTestPreduzeceNotFound() {
        given(userFeignClient.getCurrentUser(MOCK_TOKEN)).willReturn(ResponseEntity.ok(userResponse));
        given(bilansService.findBilans(eq(true), anyList(), anyList())).willReturn(bilansResponseListMap);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN)).willReturn(ResponseEntity.ok(null));

        TableReport result = (TableReport) izvestajService.makeBilansTableReport(MOCK_PREDUZECE_ID,
                MOCK_TOKEN,
                MOCK_TITLE,
                List.of(MOCK_DATUM_OD),
                List.of(MOCK_DATUM_DO),
                false);
        assertEquals(MOCK_NAME, result.getAuthor());
        assertEquals(MOCK_TITLE, result.getTitle());
        assertEquals(BilansTableContent.BILANS_COLUMNS_SINGLE_PERIOD, result.getColumns());
    }

    @Test
    void makePromenaNaKapitalTableReportTest() throws DocumentException {
        PromenaNaKapitalHelper promenaNaKapitalHelper = new PromenaNaKapitalHelper(2020, 2021, "", bilansService);
        TableReport expected = (TableReport) promenaNaKapitalHelper.makePromenaNaKapitalTableReport();
        TableReport result = (TableReport) izvestajService.makePromenaNaKapitalTableReport(2020, 2021, "");
        assertEquals(expected.getRows(), result.getRows());
        assertEquals(expected.getColumns(), result.getColumns());
    }

    @Test
    void makeStatickiIzvestajOTransakcijamaTableReport() {
        Preduzece p = Mockito.mock(Preduzece.class);
        TransakcijaResponse transakcijaResponse = Mockito.mock(TransakcijaResponse.class);
        List<TransakcijaResponse> transakcijaResponses = List.of(transakcijaResponse);
        ResponseEntity r = Mockito.mock(ResponseEntity.class);
        given(transakcijaResponse.getTipTransakcije()).willReturn(TipTransakcije.ISPLATA);
        given(transakcijaResponse.getSifraTransakcije()).willReturn(Mockito.mock(SifraTransakcijeResponse.class));
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN)).willReturn(r);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN).getBody()).willReturn(p);
        given(transakcijaService.findByPreduzeceId(MOCK_PREDUZECE_ID)).willReturn(transakcijaResponses);

        TableReport expected = (TableReport) new StatickiIzvestajOTransakcijamaHelper(MOCK_NAZIV, p, transakcijaResponses).makeTableReport();
        TableReport result = (TableReport) izvestajService.makeStatickiIzvestajOTransakcijamaTableReport(MOCK_PREDUZECE_ID, MOCK_NAZIV, null, null, MOCK_TOKEN);

        assertEquals(expected.getRows(), result.getRows());
        assertEquals(expected.getColumns(), result.getColumns());
    }

    @Test
    void makeStatickiIzvestajOTransakcijamaTableReport2() {
        Preduzece p = Mockito.mock(Preduzece.class);
        TransakcijaResponse transakcijaResponse = Mockito.mock(TransakcijaResponse.class);
        List<TransakcijaResponse> transakcijaResponses = List.of(transakcijaResponse);
        ResponseEntity r = Mockito.mock(ResponseEntity.class);
        given(transakcijaResponse.getTipTransakcije()).willReturn(TipTransakcije.ISPLATA);
        given(transakcijaResponse.getSifraTransakcije()).willReturn(Mockito.mock(SifraTransakcijeResponse.class));
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN)).willReturn(r);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN).getBody()).willReturn(p);
        given(transakcijaService.findByPreduzeceIdAndDate(MOCK_PREDUZECE_ID, MOCK_DATUM_OD, MOCK_DATUM_DO)).willReturn(transakcijaResponses);

        TableReport expected = (TableReport) new StatickiIzvestajOTransakcijamaHelper(MOCK_NAZIV, p, transakcijaResponses).makeTableReport();
        TableReport result = (TableReport) izvestajService.makeStatickiIzvestajOTransakcijamaTableReport(MOCK_PREDUZECE_ID, MOCK_NAZIV, MOCK_DATUM_OD, MOCK_DATUM_DO, MOCK_TOKEN);

        assertEquals(expected.getRows(), result.getRows());
        assertEquals(expected.getColumns(), result.getColumns());
    }

    @Test
    void makeStatickiIzvestajOTransakcijamaTableReportNotFound() {
        ResponseEntity r = Mockito.mock(ResponseEntity.class);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN)).willReturn(r);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN).getBody()).willReturn(null);
        assertThrows(EntityNotFoundException.class, () -> izvestajService.makeStatickiIzvestajOTransakcijamaTableReport(MOCK_PREDUZECE_ID, MOCK_NAZIV, null, null, MOCK_TOKEN));
    }

    @Test
    void makeSifraTransakcijaTableReportTest() {

        SifraTransakcijeResponse response = new SifraTransakcijeResponse();
        response.setSifra(1L);
        response.setUplata(10.0);
        response.setIsplata(10.0);
        response.setSaldo(0.0);
        Page<SifraTransakcijeResponse> page = new PageImpl<>(List.of(response));
        given(sifraTransakcijeService.search(any(Specification.class), eq(Pageable.unpaged()), eq(MOCK_TOKEN))).willReturn(page);

        TableReport expected = (TableReport) new SifraTransakcijaHelper(MOCK_NAZIV, page.getContent(), "saldo").makeReport();
        TableReport result = (TableReport) izvestajService.makeSifraTransakcijaTableReport(MOCK_NAZIV, "saldo", MOCK_TOKEN);

        assertEquals(expected.getRows(), result.getRows());
        assertEquals(expected.getColumns(), result.getColumns());
    }
}
