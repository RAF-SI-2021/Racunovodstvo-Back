package raf.si.racunovodstvo.nabavka.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.responses.KalkulacijaResponse;

@Component
public class KalkulacijaReverseConverter implements IConverter<Kalkulacija, KalkulacijaResponse> {

    private final ModelMapper modelMapper;

    public KalkulacijaReverseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public KalkulacijaResponse convert(Kalkulacija source) {
        return modelMapper.map(source, KalkulacijaResponse.class);
    }
}
