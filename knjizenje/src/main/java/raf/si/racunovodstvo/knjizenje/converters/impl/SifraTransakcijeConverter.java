package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.requests.SifraTransakcijeRequest;

@Component
public class SifraTransakcijeConverter implements IConverter<SifraTransakcijeRequest, SifraTransakcije> {

    private final ModelMapper modelMapper;

    public SifraTransakcijeConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SifraTransakcije convert(SifraTransakcijeRequest source) {
        SifraTransakcije converted;
        converted = modelMapper.map(source, SifraTransakcije.class);
        return converted;
    }

}