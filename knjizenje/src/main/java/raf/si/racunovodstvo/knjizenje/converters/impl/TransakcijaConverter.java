package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.requests.TransakcijaRequest;

@Component
public class TransakcijaConverter implements IConverter<TransakcijaRequest, Transakcija> {

    private final ModelMapper modelMapper;

    public TransakcijaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Transakcija convert(TransakcijaRequest source) {
        Transakcija converted;
        converted = modelMapper.map(source, Transakcija.class);
        return converted;
    }

}