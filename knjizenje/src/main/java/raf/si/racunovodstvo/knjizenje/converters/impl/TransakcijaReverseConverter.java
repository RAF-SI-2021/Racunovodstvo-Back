package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;

@Component
public class TransakcijaReverseConverter implements IConverter<Transakcija, TransakcijaResponse> {

    private final ModelMapper modelMapper;

    public TransakcijaReverseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TransakcijaResponse convert(Transakcija source) {
        TransakcijaResponse transakcijaResponse = modelMapper.map(source, TransakcijaResponse.class);
        transakcijaResponse.setTransakcijaId(source.getDokumentId());
        return transakcijaResponse;
    }
}
