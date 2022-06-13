package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.knjizenje.reports.schema.BilansSchema;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BilansSchemaConverterTest {

    @InjectMocks
    private BilansSchemaConverter bilansSchemaConverter;

    private static final Double MOCK_DUGUJE = 2.0;
    private static final Double MOCK_POTRAZUJE = 1.0;
    private static final Long MOCK_BROJ_STAVKI = 3L;
    private static final String MOCK_BROJ_KONTA = "MOCK_BROJ";
    private static final String MOCK_NAZIV_KONTA = "MOCK_NAZIV";

    @Test
    void convertTest() {
        BilansResponse bilansResponse =
            new BilansResponse(MOCK_DUGUJE, MOCK_POTRAZUJE, MOCK_BROJ_STAVKI, MOCK_BROJ_KONTA, MOCK_NAZIV_KONTA);

        BilansSchema bilansSchema = bilansSchemaConverter.convert(bilansResponse);

        assertEquals(bilansResponse.getPotrazuje(), Double.parseDouble(bilansSchema.getPotrazuje()));
        assertEquals(bilansResponse.getDuguje(), Double.parseDouble(bilansSchema.getDuguje()));
        assertEquals(bilansResponse.getBrojStavki(), Integer.parseInt(bilansSchema.getStavki()));
        assertEquals(bilansResponse.getSaldo(), Double.parseDouble(bilansSchema.getSaldo()));
        assertEquals(bilansResponse.getBrojKonta(), bilansSchema.getKonto());
        assertEquals(bilansResponse.getNazivKonta(), bilansSchema.getNaziv());
    }
}
