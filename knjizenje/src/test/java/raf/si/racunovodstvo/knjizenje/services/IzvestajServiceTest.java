package raf.si.racunovodstvo.knjizenje.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.converter.BilansSchemaConverter;
import raf.si.racunovodstvo.knjizenje.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.knjizenje.feign.UserFeignClient;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.reports.BilansTableContent;
import raf.si.racunovodstvo.knjizenje.reports.TableReport;
import raf.si.racunovodstvo.knjizenje.reports.schema.BilansSchema;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.responses.UserResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IBilansService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IzvestajServiceTest {

    @InjectMocks
    private IzvestajService izvestajService;

    @Mock
    private IBilansService bilansService;

    @Mock
    private BilansSchemaConverter bilansSchemaConverter;

    @Mock
    private PreduzeceFeignClient preduzeceFeignClient;

    @Mock
    private UserFeignClient userFeignClient;

    private UserResponse userResponse;
    private List<BilansResponse> bilansResponseList;

    private Map<String,List<BilansResponse>> bilansResponseListMap;

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

        bilansResponseListMap = Map.of("",List.of(bilansResponse));
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
        given(bilansService.findBilans(anyList(), anyList(), anyList())).willReturn(bilansResponseListMap);
        given(preduzeceFeignClient.getPreduzeceById(MOCK_PREDUZECE_ID, MOCK_TOKEN)).willReturn(ResponseEntity.ok(preduzece));

        TableReport result = (TableReport) izvestajService.makeBilansTableReport(MOCK_PREDUZECE_ID,
                                                                                 MOCK_TOKEN,
                                                                                 MOCK_TITLE,
                                                                                 List.of(MOCK_DATUM_OD),
                                                                                 List.of(MOCK_DATUM_DO),
                                                                                 new ArrayList<>(),false);
        assertEquals(MOCK_NAME, result.getAuthor());
        assertEquals(MOCK_TITLE, result.getTitle());
        assertEquals(BilansTableContent.BILANS_COLUMNS_SINGLE_PERIOD, result.getColumns());
        assertTrue(result.getFooter().contains(MOCK_ADRESA));
        assertTrue(result.getFooter().contains(MOCK_GRAD));
        assertTrue(result.getFooter().contains(MOCK_NAZIV));
    }
}
