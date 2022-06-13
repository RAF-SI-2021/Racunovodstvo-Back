package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TransakcijaReverseConverterTest {

    @InjectMocks
    private TransakcijaReverseConverter transakcijaReverseConverter;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void convertTest() {
        Transakcija transakcija = new Transakcija();
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        given(modelMapper.map(transakcija, TransakcijaResponse.class)).willReturn(transakcijaResponse);

        assertEquals(transakcijaResponse, transakcijaReverseConverter.convert(transakcija));
    }
}
