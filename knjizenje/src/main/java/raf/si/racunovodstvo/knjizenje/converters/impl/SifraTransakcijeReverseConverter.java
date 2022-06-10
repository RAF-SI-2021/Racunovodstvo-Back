package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;

@Component
public class SifraTransakcijeReverseConverter implements IConverter<SifraTransakcije, SifraTransakcijeResponse> {

    private final ModelMapper modelMapper;

    public SifraTransakcijeReverseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SifraTransakcijeResponse convert(SifraTransakcije source) {
        SifraTransakcijeResponse transakcijaResponse = modelMapper.map(source, SifraTransakcijeResponse.class);
        transakcijaResponse.setSifraTransakcijeId(source.getSifraTransakcijeId());
        return transakcijaResponse;
    }
}
