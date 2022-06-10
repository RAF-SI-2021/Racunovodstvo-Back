package raf.si.racunovodstvo.nabavka.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.requests.KalkulacijaRequest;

@Component
public class KalkulacijaConverter implements IConverter<KalkulacijaRequest, Kalkulacija> {

    private final ModelMapper modelMapper;

    public KalkulacijaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Kalkulacija convert(KalkulacijaRequest source) {
        // TODO izvuci Lokaciju iz source.getLokacijaId()
        return modelMapper.map(source, Kalkulacija.class);
    }
}
