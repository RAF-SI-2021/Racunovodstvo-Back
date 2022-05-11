package raf.si.racunovodstvo.nabavka.converters.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.model.KalkulacijaArtikal;
import raf.si.racunovodstvo.nabavka.model.KonverzijaArtikal;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArtikalConverterTest {

    @InjectMocks
    private ArtikalConverter artikalConverter;

    @Mock
    private ModelMapper modelMapper;

    /*
    @Test
    void convertKonverzijaArtikalTest() {
        KonverzijaArtikal konverzijaArtikal = new KonverzijaArtikal();
        ArtikalRequest source = new ArtikalRequest();
        konverzijaArtikal.setNabavnaCena(20.0);
        konverzijaArtikal.setRabatProcenat(10.0);
        konverzijaArtikal.setKolicina(20);
        source.setAktivanZaProdaju(false);
        given(modelMapper.map(source, KonverzijaArtikal.class)).willReturn(konverzijaArtikal);

        Artikal result = artikalConverter.convert(source);

        assertEquals(konverzijaArtikal, result);
    }

    @Test
    void convertKalkulacijaArtikalTest() {
        KalkulacijaArtikal kalkulacijaArtikal = new KalkulacijaArtikal();
        ArtikalRequest source = new ArtikalRequest();
        kalkulacijaArtikal.setNabavnaCena(20.0);
        kalkulacijaArtikal.setRabatProcenat(10.0);
        kalkulacijaArtikal.setKolicina(20);
        kalkulacijaArtikal.setProdajnaCena(10.0);
        kalkulacijaArtikal.setPorezProcenat(20.0);
        kalkulacijaArtikal.setMarzaProcenat(20.0);
        source.setAktivanZaProdaju(true);
        given(modelMapper.map(source, KalkulacijaArtikal.class)).willReturn(kalkulacijaArtikal);

        Artikal result = artikalConverter.convert(source);

        assertEquals(kalkulacijaArtikal, result);
    }
     */
}
