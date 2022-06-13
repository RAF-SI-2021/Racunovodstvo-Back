package raf.si.racunovodstvo.preduzece.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.preduzece.converters.IConverter;
import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaposleniRequest;
import raf.si.racunovodstvo.preduzece.services.IObracunService;
import raf.si.racunovodstvo.preduzece.services.IZaposleniService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Component
public class ObracunZaposleniConverter implements IConverter<ObracunZaposleniRequest, ObracunZaposleni> {

    private final ModelMapper modelMapper;
    private final IZaposleniService zaposleniService;
    private final IObracunService iObracunService;

    public ObracunZaposleniConverter(ModelMapper modelMapper, IZaposleniService zaposleniService, IObracunService iObracunService){
        this.modelMapper=modelMapper;

        this.zaposleniService = zaposleniService;
        this.iObracunService = iObracunService;
    }

    @Override
    public ObracunZaposleni convert(ObracunZaposleniRequest source) {

        Optional<Zaposleni> zaposleni = zaposleniService.findById(source.getZaposleniId());
        Optional<Obracun> obracun = iObracunService.findById(source.getObracunId());
        ObracunZaposleni obracunZaposleni = modelMapper.map(source, ObracunZaposleni.class);

        if(zaposleni.isPresent()) {
            obracunZaposleni.setZaposleni(zaposleni.get());
        }else{
            throw new EntityNotFoundException();
        }

        if(obracun.isPresent()){
            obracunZaposleni.setObracun(obracun.get());
        }else{
            throw new EntityNotFoundException();
        }

        return obracunZaposleni;
    }
}
