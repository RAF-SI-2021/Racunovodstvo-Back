package raf.si.racunovodstvo.knjizenje.converter.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.converter.IConverter;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.responses.AnalitickaKarticaResponse;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnalitickaKarticaConverter implements IConverter<Knjizenje, AnalitickaKarticaResponse> {

    private final ModelMapper modelMapper;

    public AnalitickaKarticaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AnalitickaKarticaResponse convert(Knjizenje source) {
        AnalitickaKarticaResponse response = modelMapper.map(source, AnalitickaKarticaResponse.class);
        double duguje = 0.0;
        double potrazuje = 0.0;
        double saldo;
        response.setBrojDokumenta(source.getDokument().getBrojDokumenta());
        for (Konto konto : source.getKonto()) {
            duguje += konto.getDuguje();
            potrazuje += konto.getPotrazuje();
        }
        saldo = duguje - potrazuje;
        response.setDuguje(duguje);
        response.setPotrazuje(potrazuje);
        response.setSaldo(saldo);
        return response;
    }
}

