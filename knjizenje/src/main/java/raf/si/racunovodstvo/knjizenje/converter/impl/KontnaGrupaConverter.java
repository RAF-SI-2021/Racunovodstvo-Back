package raf.si.racunovodstvo.knjizenje.converter.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.converter.IConverter;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;
import raf.si.racunovodstvo.knjizenje.requests.AnalitickaKarticaRequest;

@Component
public class KontnaGrupaConverter implements IConverter<AnalitickaKarticaRequest, KontnaGrupa> {

    private final ModelMapper modelMapper;

    public KontnaGrupaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public KontnaGrupa convert(AnalitickaKarticaRequest source) {
        return modelMapper.map(source, KontnaGrupa.class);
    }
}
