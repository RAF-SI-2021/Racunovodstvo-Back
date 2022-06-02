package raf.si.racunovodstvo.preduzece.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.converters.IConverter;
import raf.si.racunovodstvo.preduzece.converters.impl.ObracunZaposleniConverter;
import raf.si.racunovodstvo.preduzece.model.Koeficijent;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaposleniRepository;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaposleniRequest;
import raf.si.racunovodstvo.preduzece.services.IObracunZaposleniService;


import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ObracunZaposleniService implements IObracunZaposleniService {

    private final ObracunZaposleniRepository obracunZaposleniRepository;

    private final IConverter<ObracunZaposleniRequest, ObracunZaposleni> obracunZaposleniConverter;
    private final KoeficijentService koeficijentService;

    public ObracunZaposleniService(ObracunZaposleniRepository obracunZaposleniRepository, ObracunZaposleniConverter obracunZaposleniConverter, KoeficijentService koeficijentService) {
        this.obracunZaposleniRepository = obracunZaposleniRepository;
        this.obracunZaposleniConverter = obracunZaposleniConverter;
        this.koeficijentService = koeficijentService;
    }

    @Override
    public <S extends ObracunZaposleni> S save(S var1) {
        return obracunZaposleniRepository.save(var1);
    }

    @Override
    public Optional<ObracunZaposleni> findById(Long var1) {
        return obracunZaposleniRepository.findById(var1);
    }

    @Override
    public List<ObracunZaposleni> findAll() {
        return obracunZaposleniRepository.findAll();
    }

    @Override
    public void deleteById(Long obracunZaposleniId) {
        Optional<ObracunZaposleni> optionalObracunZaposleni = obracunZaposleniRepository.findById(obracunZaposleniId);
        if (optionalObracunZaposleni.isEmpty()) {
            throw new EntityNotFoundException();
        }
        obracunZaposleniRepository.deleteById(obracunZaposleniId);
    }

    @Override
    public ObracunZaposleni save(ObracunZaposleniRequest obracunZaposleniRequest) {

        ObracunZaposleni obracunZaposleni = obracunZaposleniConverter.convert(obracunZaposleniRequest);
        if(obracunZaposleniRepository.findByZaposleniAndObracun(obracunZaposleni.getZaposleni(), obracunZaposleni.getObracun()).isPresent()){
            throw new EntityExistsException();
        }
        izracunajUcinak(koeficijentService.getCurrentKoeficijent(), obracunZaposleni);

        return obracunZaposleniRepository.save(obracunZaposleni);
    }

    @Override
    public ObracunZaposleni update(ObracunZaposleniRequest obracunZaposleniRequest) {

        Optional<ObracunZaposleni> optionalObracunZaposleni = obracunZaposleniRepository.findById(obracunZaposleniRequest.getObracunZaposleniId());

        if(optionalObracunZaposleni.isEmpty()){
            throw new EntityNotFoundException();
        }

        ObracunZaposleni obracunZaposleni = obracunZaposleniConverter.convert(obracunZaposleniRequest);
        izracunajUcinak(koeficijentService.getCurrentKoeficijent(), obracunZaposleni);

        return obracunZaposleniRepository.save(obracunZaposleni);
    }

    @Override
    public Page<ObracunZaposleni> findAll(Specification<ObracunZaposleni> spec, Pageable pageSort) {
        return obracunZaposleniRepository.findAll(spec, pageSort);
    }

    @Override
    public Page<ObracunZaposleni> findAll(Pageable pageSort) {
        return obracunZaposleniRepository.findAll(pageSort);
    }

    private void izracunajUcinak(Koeficijent koeficijent, ObracunZaposleni obracunZaposleni) {
        double b;
        double netoPlata = obracunZaposleni.getNetoPlata()*obracunZaposleni.getUcinak();
        if (netoPlata < koeficijent.getNajnizaOsnovica()) {
            b = (netoPlata - 1.93 + (koeficijent.getNajnizaOsnovica() * 19.9)) / 0.9;
        }
        else if (netoPlata < koeficijent.getNajvisaOsnovica()) {
            b = (netoPlata - 1.93) / 0.701;
        }
        else {
            b = (netoPlata - 1.93 + (koeficijent.getNajvisaOsnovica() * 19.9)) / 0.9;
        }
        obracunZaposleni.setDoprinos1( b * (koeficijent.getPenzionoOsiguranje1() + koeficijent.getZdravstvenoOsiguranje1() + koeficijent.getNezaposlenost1()));
        obracunZaposleni.setDoprinos2(b * (koeficijent.getPenzionoOsiguranje2() + koeficijent.getZdravstvenoOsiguranje2() + koeficijent.getNezaposlenost2()));
        obracunZaposleni.setPorez((b - koeficijent.getPoreskoOslobadjanje()) * koeficijent.getKoeficijentPoreza());
        obracunZaposleni.setBrutoPlata(netoPlata + obracunZaposleni.getPorez());
        obracunZaposleni.setUkupanTrosakZarade(obracunZaposleni.getBrutoPlata() + obracunZaposleni.getDoprinos2());
    }

}
