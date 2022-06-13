package raf.si.racunovodstvo.knjizenje.converters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import raf.si.racunovodstvo.knjizenje.converters.impl.KontoConverter;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.responses.GlavnaKnjigaResponse;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class KontoConverterTest {

    @InjectMocks
    private KontoConverter kontoConverter;

    private static final Long MOCK_KNJIZENJE_ID = 1L;
    private static final Double MOCK_DUGUJE = 2.0;
    private static final Double MOCK_POTRAZUJE = 2.0;
    private static final String MOCK_NAZIV_KONTA = "DUMMY_NAZIV_KONTA";
    private static final String MOCK_BROJ_KONTA = "DUMMY_BROJ_KONTA";
    private static final String MOCK_KOMENTAR = "DUMMY_KOMENTAR";
    private static final Date MOCK_DATUM_KNJIZENJA = new Date();

    @Test
    void convertTest() {
        Konto konto = new Konto();
        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(MOCK_KNJIZENJE_ID);
        knjizenje.setKomentar(MOCK_KOMENTAR);
        knjizenje.setDatumKnjizenja(MOCK_DATUM_KNJIZENJA);
        KontnaGrupa kontnaGrupa = new KontnaGrupa();
        kontnaGrupa.setNazivKonta(MOCK_NAZIV_KONTA);
        kontnaGrupa.setBrojKonta(MOCK_BROJ_KONTA);
        konto.setDuguje(MOCK_DUGUJE);
        konto.setPotrazuje(MOCK_POTRAZUJE);
        konto.setKnjizenje(knjizenje);
        konto.setKontnaGrupa(kontnaGrupa);
        List<Konto> kontoList = List.of(konto);

        Page<GlavnaKnjigaResponse> response = kontoConverter.convert(kontoList);

        assertEquals(1, response.getTotalElements());
        response.getContent().forEach(knjiga -> {
            assertEquals(MOCK_BROJ_KONTA, knjiga.getKonto());
            assertEquals(MOCK_DUGUJE, knjiga.getDuguje());
            assertEquals(MOCK_POTRAZUJE, knjiga.getPotrazuje());
            assertEquals(MOCK_DUGUJE - MOCK_POTRAZUJE, knjiga.getSaldo());
            assertEquals(MOCK_KOMENTAR, knjiga.getKomentar());
            assertEquals(MOCK_DATUM_KNJIZENJA, knjiga.getDatum());
            assertEquals(MOCK_NAZIV_KONTA, knjiga.getNazivKonta());
            assertEquals(MOCK_KNJIZENJE_ID, knjiga.getBrojNaloga());
        });
    }
}
