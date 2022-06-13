package raf.si.racunovodstvo.knjizenje.converters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import raf.si.racunovodstvo.knjizenje.converter.impl.KnjizenjeConverter;
import raf.si.racunovodstvo.knjizenje.model.Dokument;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.responses.KnjizenjeResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IKnjizenjeService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KnjizenjeConverterTest {

    @InjectMocks
    private KnjizenjeConverter knjizenjeConverter;

    @Mock
    private IKnjizenjeService knjizenjeService;

    private static final Long MOCK_KNJIZENJE_ID = 1L;
    private static final Long MOCK_DOKUMENT_ID = 2L;
    private static final Double MOCK_SALDO = 2.0;
    private static final Double MOCK_POTRAZUJE = 2.0;
    private static final Double MOCK_DUGUJE = 4.0;
    private static final Date MOCK_DATUM_KNJIZENJA = new Date();
    private static final String MOCK_BROJ_NALOGA = "DUMMY_BROJ_NALOGA";
    private static final String MOCK_KOMENTAR = "DUMMY_KOMENTAR";

    @Test
    void convertTest() {
        Knjizenje knjizenje = new Knjizenje();
        Dokument dokument = new Dokument();
        dokument.setDokumentId(MOCK_DOKUMENT_ID);
        Konto konto = new Konto();
        List<Konto> kontoList = List.of(konto);
        knjizenje.setKnjizenjeId(MOCK_KNJIZENJE_ID);
        knjizenje.setDatumKnjizenja(MOCK_DATUM_KNJIZENJA);
        knjizenje.setDokument(dokument);
        knjizenje.setKonto(kontoList);
        knjizenje.setBrojNaloga(MOCK_BROJ_NALOGA);
        knjizenje.setKomentar(MOCK_KOMENTAR);
        List<Knjizenje> knjizenjeList = List.of(knjizenje);
        given(knjizenjeService.getSaldoZaKnjizenje(MOCK_KNJIZENJE_ID)).willReturn(MOCK_SALDO);
        given(knjizenjeService.getSumaDugujeZaKnjizenje(MOCK_KNJIZENJE_ID)).willReturn(MOCK_DUGUJE);
        given(knjizenjeService.getSumaPotrazujeZaKnjizenje(MOCK_KNJIZENJE_ID)).willReturn(MOCK_POTRAZUJE);

        Page<KnjizenjeResponse> result = knjizenjeConverter.convert(knjizenjeList);

        assertEquals(1, result.getTotalElements());
        result.getContent().forEach(response -> {
            assertEquals(MOCK_DUGUJE, response.getDuguje());
            assertEquals(MOCK_POTRAZUJE, response.getPotrazuje());
            assertEquals(MOCK_SALDO, response.getSaldo());
            assertEquals(knjizenje.getKomentar(), response.getKomentar());
            assertEquals(knjizenje.getDokument().getDokumentId(), response.getDokumentId());
            assertEquals(knjizenje.getKnjizenjeId(), response.getKnjizenjeId());
            assertEquals(knjizenje.getBrojNaloga(), response.getBrojNaloga());
            assertEquals(knjizenje.getDatumKnjizenja(), response.getDatumKnjizenja());
        });
    }
}
