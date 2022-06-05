package raf.si.racunovodstvo.preduzece.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.converters.IConverter;
import raf.si.racunovodstvo.preduzece.converters.impl.ObracunZaposleniConverter;
import raf.si.racunovodstvo.preduzece.model.*;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;
import raf.si.racunovodstvo.preduzece.repositories.ObracunRepository;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaposleniRepository;
import raf.si.racunovodstvo.preduzece.repositories.PlataRepository;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaposleniRequest;
import raf.si.racunovodstvo.preduzece.services.IObracunZaposleniService;


import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ObracunZaposleniService implements IObracunZaposleniService {

    private final ObracunZaposleniRepository obracunZaposleniRepository;
    private final ObracunRepository obracunRepository;
    private final PlataRepository plataRepository;

    private final IConverter<ObracunZaposleniRequest, ObracunZaposleni> obracunZaposleniConverter;
    private final KoeficijentService koeficijentService;

    private static final Double DEFAULT_UCINAK = 100.0;

    public ObracunZaposleniService(ObracunZaposleniRepository obracunZaposleniRepository, ObracunRepository obracunRepository, PlataRepository plataRepository, ObracunZaposleniConverter obracunZaposleniConverter, KoeficijentService koeficijentService) {
        this.obracunZaposleniRepository = obracunZaposleniRepository;
        this.obracunRepository = obracunRepository;
        this.plataRepository = plataRepository;
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
        if (obracunZaposleniRepository.findByZaposleniAndObracun(obracunZaposleni.getZaposleni(), obracunZaposleni.getObracun()).isPresent()) {
            throw new EntityExistsException();
        }
        izracunajUcinak(koeficijentService.getCurrentKoeficijent(), obracunZaposleni);

        return obracunZaposleniRepository.save(obracunZaposleni);
    }

    @Override
    public ObracunZaposleni update(Double ucinak, Double netoPlata, Long idObracunZaposleni) {
        Optional<ObracunZaposleni> optionalObracunZaposleni = obracunZaposleniRepository.findById(idObracunZaposleni);

        if (optionalObracunZaposleni.isEmpty()) {
            throw new EntityNotFoundException();
        }

        ObracunZaposleni obracunZaposleni = optionalObracunZaposleni.get();

        if(ucinak != null){
            obracunZaposleni.setUcinak(ucinak);
        }

        if(netoPlata != null){
            obracunZaposleni.setNetoPlata(netoPlata);
        }

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
        double netoPlata = obracunZaposleni.getNetoPlata() * obracunZaposleni.getUcinak();
        if (netoPlata < koeficijent.getNajnizaOsnovica()) {
            b = (netoPlata - 1.93 + (koeficijent.getNajnizaOsnovica() * 19.9)) / 0.9;
        } else if (netoPlata < koeficijent.getNajvisaOsnovica()) {
            b = (netoPlata - 1.93) / 0.701;
        } else {
            b = (netoPlata - 1.93 + (koeficijent.getNajvisaOsnovica() * 19.9)) / 0.9;
        }
        obracunZaposleni.setDoprinos1(b * (koeficijent.getPenzionoOsiguranje1() + koeficijent.getZdravstvenoOsiguranje1() + koeficijent.getNezaposlenost1()));
        obracunZaposleni.setDoprinos2(b * (koeficijent.getPenzionoOsiguranje2() + koeficijent.getZdravstvenoOsiguranje2() + koeficijent.getNezaposlenost2()));
        obracunZaposleni.setPorez((b - koeficijent.getPoreskoOslobadjanje()) * koeficijent.getKoeficijentPoreza());
        obracunZaposleni.setBrutoPlata(netoPlata + obracunZaposleni.getPorez());
        obracunZaposleni.setUkupanTrosakZarade(obracunZaposleni.getBrutoPlata() + obracunZaposleni.getDoprinos2());
    }

    public void makeObracun(Date dateTime) {
        Obracun obracun = new Obracun();
        obracun.setDatumObracuna(dateTime);
        obracun.setNaziv(new SimpleDateFormat("MM/yy").format(dateTime));
        obracun = obracunRepository.save(obracun);
        List<Plata> plate = plataRepository.findPlataByDatumAndStatusZaposlenog(dateTime, StatusZaposlenog.ZAPOSLEN);
        List<ObracunZaposleni> obracunZaposleniList = new ArrayList<>();
        for (Plata plata : plate) {
            ObracunZaposleni obracunZaposleni = save(obracunZaposleniConverter.convert(makeObracunZaradeObject(plata, obracun.getObracunId())));
            obracunZaposleniList.add(obracunZaposleni);
        }
        obracun.setObracunZaposleniList(obracunZaposleniList);
        obracunRepository.save(obracun);
    }

    private ObracunZaposleniRequest makeObracunZaradeObject(Plata plata, Long obracunId) {
        ObracunZaposleniRequest obracunZarade = new ObracunZaposleniRequest();
        obracunZarade.setPorez(plata.getPorez());
        obracunZarade.setDoprinos1(plata.getDoprinos1());
        obracunZarade.setDoprinos2(plata.getDoprinos2());
        obracunZarade.setNetoPlata(plata.getNetoPlata());
        obracunZarade.setBrutoPlata(plata.getBrutoPlata());
        obracunZarade.setUkupanTrosakZarade(plata.getUkupanTrosakZarade());
        obracunZarade.setKomentar(plata.getKomentar());
        obracunZarade.setUcinak(DEFAULT_UCINAK);
        obracunZarade.setObracunId(obracunId);
        obracunZarade.setZaposleniId(plata.getZaposleni().getZaposleniId());
        return obracunZarade;
    }
}
