package raf.si.racunovodstvo.nabavka.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.responses.ArtikalResponse;

@Component
public class ArtikalReverseConverter implements IConverter<Artikal, ArtikalResponse> {

    private final ModelMapper modelMapper;

    public ArtikalReverseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ArtikalResponse convert(Artikal source) {
        ArtikalResponse artikalResponse = modelMapper.map(source, ArtikalResponse.class);
        artikalResponse.setKonverzijaKalkulacijaId(source.getBaznaKonverzijaKalkulacija().getId());
        return artikalResponse;
    }
}
