package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.requests.TransakcijaRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TransakcijaConverterTest {

    @InjectMocks
    private TransakcijaConverter transakcijaConverter;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void convertTest() {
        Transakcija transakcija = new Transakcija();
        TransakcijaRequest transakcijaRequest = new TransakcijaRequest();
        given(modelMapper.map(transakcijaRequest, Transakcija.class)).willReturn(transakcija);

        assertEquals(transakcija, transakcijaConverter.convert(transakcijaRequest));
    }
}
