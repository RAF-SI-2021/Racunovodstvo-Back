package raf.si.racunovodstvo.knjizenje.controllers;

import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import raf.si.racunovodstvo.knjizenje.reports.Reports;
import raf.si.racunovodstvo.knjizenje.services.impl.IIzvestajService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IzvestajiControllerTest {

    @InjectMocks
    private IzvestajiController izvestajiController;

    @Mock
    private IIzvestajService izvestajService;

    private static final String MOCK_TITLE = "MOCK_TITLE";
    private static final String MOCK_BROJ_KONTA_OD = "MOCK_OD";
    private static final String MOCK_BROJ_KONTA_DO = "MOCK_DO";
    private static final String MOCK_TOKEN = "MOCK_TOKEN";
    private static final String MOCK_SORT = "+saldo";
    private static final Long MOCK_PREDUZECE_ID = 1L;
    private static final Date MOCK_DATUM_OD = new Date();
    private static final Date MOCK_DATUM_DO = new Date();
    private static final Integer MOCK_GODINA_1 = 2020;
    private static final Integer MOCK_GODINA_2 = 2022;

    @Test
    void getBrutoBilansTest() throws DocumentException {
        byte[] expected = new byte[]{};
        Reports reports = Mockito.mock(Reports.class);
        given(izvestajService.makeBrutoBilansTableReport(MOCK_TOKEN,
                                                         MOCK_TITLE,
                                                         MOCK_DATUM_OD,
                                                         MOCK_DATUM_DO,
                                                         MOCK_BROJ_KONTA_OD,
                                                         MOCK_BROJ_KONTA_DO)).willReturn(reports);
        given(reports.getReport()).willReturn(expected);

        byte[] result =
            (byte[]) izvestajiController.getBrutoBilans(MOCK_TITLE,
                                                        MOCK_BROJ_KONTA_OD,
                                                        MOCK_BROJ_KONTA_DO,
                                                        MOCK_DATUM_OD,
                                                        MOCK_DATUM_DO,
                                                        MOCK_TOKEN).getBody();
        assertEquals(expected, result);
    }

    @Test
    void getBilansStanjaTest() throws DocumentException {
        byte[] expected = new byte[]{};
        Reports reports = Mockito.mock(Reports.class);
        List<Date> datumiOd = List.of(MOCK_DATUM_OD);
        List<Date> datumiDo = List.of(MOCK_DATUM_DO);
        given(izvestajService.makeBilansTableReport(MOCK_PREDUZECE_ID,
                                                    MOCK_TOKEN,
                                                    MOCK_TITLE,
                                                    datumiOd,
                                                    datumiDo,
                                                    false)).willReturn(reports);
        given(reports.getReport()).willReturn(expected);

        byte[] result =
            (byte[]) izvestajiController.getBilansStanja(MOCK_PREDUZECE_ID, MOCK_TITLE, datumiOd, datumiDo, MOCK_TOKEN)
                                        .getBody();
        assertEquals(expected, result);
    }

    @Test
    void getBilansUspehaTest() throws DocumentException {
        byte[] expected = new byte[]{};
        Reports reports = Mockito.mock(Reports.class);
        List<Date> datumiOd = List.of(MOCK_DATUM_OD);
        List<Date> datumiDo = List.of(MOCK_DATUM_DO);

        given(izvestajService.makeBilansTableReport(MOCK_PREDUZECE_ID,
                                                    MOCK_TOKEN,
                                                    MOCK_TITLE,
                                                    datumiOd,
                                                    datumiDo,
                                                    true)).willReturn(reports);
        given(reports.getReport()).willReturn(expected);

        byte[] result =
            (byte[]) izvestajiController.getBilansUspeha(MOCK_PREDUZECE_ID, MOCK_TITLE, datumiOd, datumiDo, MOCK_TOKEN)
                                        .getBody();
        assertEquals(expected, result);
    }

    @Test
    void getStatickiIzvestajiOTransakcijamaTest() throws DocumentException {
        byte[] expected = new byte[]{};
        Reports reports = Mockito.mock(Reports.class);

        given(izvestajService.makeStatickiIzvestajOTransakcijamaTableReport(MOCK_PREDUZECE_ID,
                MOCK_TITLE,
                MOCK_DATUM_OD,
                MOCK_DATUM_DO,
                MOCK_TOKEN
                )).willReturn(reports);
        given(reports.getReport()).willReturn(expected);

        byte[] result =
                (byte[]) izvestajiController.getStatickiIzvestajOTransakcijama(MOCK_PREDUZECE_ID, MOCK_TITLE, MOCK_DATUM_OD, MOCK_DATUM_DO, MOCK_TOKEN)
                        .getBody();
        assertEquals(expected, result);
    }

    @Test
    void getSifraTransakcijeTest() throws DocumentException {
        byte[] expected = new byte[]{};
        Reports reports = Mockito.mock(Reports.class);

        given(izvestajService.makeSifraTransakcijaTableReport(
                MOCK_TITLE,
                MOCK_SORT,
                MOCK_TOKEN
        )).willReturn(reports);
        given(reports.getReport()).willReturn(expected);

        byte[] result =
                (byte[]) izvestajiController.getSifraTransakcije(MOCK_TITLE, MOCK_SORT, MOCK_TOKEN)
                        .getBody();
        assertEquals(expected, result);
    }

    @Test
    void getPromenaNaKapitalTest() throws DocumentException {
        byte[] expected = new byte[]{};
        Reports reports = Mockito.mock(Reports.class);

        given(izvestajService.makePromenaNaKapitalTableReport(
                MOCK_GODINA_1,
                MOCK_GODINA_2,
                MOCK_TITLE
        )).willReturn(reports);
        given(reports.getReport()).willReturn(expected);

        byte[] result =
                (byte[]) izvestajiController.getPromenaNaKapital(MOCK_GODINA_1, MOCK_GODINA_2, MOCK_TITLE, MOCK_TOKEN)
                        .getBody();
        assertEquals(expected, result);
    }

}
