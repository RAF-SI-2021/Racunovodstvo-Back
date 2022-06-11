package raf.si.racunovodstvo.nabavka.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;

@Component
public class KonverzijaConverter implements IConverter<Konverzija, KonverzijaResponse> {

    private final ModelMapper modelMapper;

    public KonverzijaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public KonverzijaResponse convert(Konverzija konverzija) {
        KonverzijaResponse response = modelMapper.map(konverzija, KonverzijaResponse.class);
        response.setKonverzijaId(konverzija.getId());
        Double troskoviNabavke = konverzija.getTroskoviNabavke().stream().mapToDouble(TroskoviNabavke::getCena).sum();
        response.setTroskoviNabavkeSum(troskoviNabavke);
        return response;
    }
}
