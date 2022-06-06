package raf.si.racunovodstvo.nabavka.converters.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.model.BaznaKonverzijaKalkulacija;
import raf.si.racunovodstvo.nabavka.responses.ArtikalResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArtikalReverseConverterTest {

    @InjectMocks
    private ArtikalReverseConverter artikalReverseConverter;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void convertTest() {
        Artikal source = Mockito.mock(Artikal.class);
        ArtikalResponse expectedResponse = new ArtikalResponse();
        given(modelMapper.map(source, ArtikalResponse.class)).willReturn(expectedResponse);
        given(source.getBaznaKonverzijaKalkulacija()).willReturn(new BaznaKonverzijaKalkulacija());

        ArtikalResponse actualResponse = artikalReverseConverter.convert(source);

        assertEquals(expectedResponse, actualResponse);
    }
}
