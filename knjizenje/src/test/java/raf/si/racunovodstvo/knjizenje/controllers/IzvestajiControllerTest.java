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
    private static final String MOCK_NAME = "MOCK_NAME";
    private static final String MOCK_TOKEN = "MOCK_TOKEN";
    private static final Long MOCK_PREDUZECE_ID = 1L;
    private static final Date MOCK_DATUM_OD = new Date();
    private static final Date MOCK_DATUM_DO = new Date();

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
        List<String> startsWith = List.of("5", "6");
        given(izvestajService.makeBilansTableReport(MOCK_PREDUZECE_ID,
                                                    MOCK_TOKEN,
                                                    MOCK_TITLE,
                                                    datumiOd,
                                                    datumiDo,
                                                    startsWith)).willReturn(reports);
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
        List<String> startsWith = List.of("0", "1", "2", "3", "4");
        given(izvestajService.makeBilansTableReport(MOCK_PREDUZECE_ID,
                                                    MOCK_TOKEN,
                                                    MOCK_TITLE,
                                                    datumiOd,
                                                    datumiDo,
                                                    startsWith)).willReturn(reports);
        given(reports.getReport()).willReturn(expected);

        byte[] result =
            (byte[]) izvestajiController.getBilansUspeha(MOCK_PREDUZECE_ID, MOCK_TITLE, datumiOd, datumiDo, MOCK_TOKEN)
                                        .getBody();
        assertEquals(expected, result);
    }

    @GetMapping(path = "/uspeh", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getBilansUspeha(@RequestParam Long preduzece,
                                             @RequestParam String title,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiOd,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiDo,
                                             @RequestHeader("Authorization") String token) throws DocumentException {
        List<String> brojKontaStartsWith = List.of("0", "1", "2", "3", "4");

        byte[] pdf =
            izvestajService.makeBilansTableReport(preduzece, token, title, datumiOd, datumiDo, brojKontaStartsWith).getReport();
        return ResponseEntity.ok(pdf);
    }
}
