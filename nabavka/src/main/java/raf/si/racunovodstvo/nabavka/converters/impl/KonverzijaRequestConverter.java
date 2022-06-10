package raf.si.racunovodstvo.nabavka.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.requests.KonverzijaRequest;

@Component
public class KonverzijaRequestConverter implements IConverter<KonverzijaRequest, Konverzija> {

    private final ModelMapper modelMapper;

    public KonverzijaRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Konverzija convert(KonverzijaRequest source) {
        return modelMapper.map(source, Konverzija.class);
    }
}
