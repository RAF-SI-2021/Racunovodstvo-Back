package raf.si.racunovodstvo.preduzece.services.impl;

import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.model.ObracunZarade;
import raf.si.racunovodstvo.preduzece.model.Plata;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaposleniRepository;
import raf.si.racunovodstvo.preduzece.repositories.ObracunZaradeRepository;
import raf.si.racunovodstvo.preduzece.repositories.PlataRepository;
import raf.si.racunovodstvo.preduzece.services.IService;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ObracunZaradeService implements IService<ObracunZarade, Long> {
    private final ObracunZaradeRepository obracunZaradeRepository;
    private final PlataRepository plataRepository;
    private final ObracunZaposleniRepository obracunZaposleniRepository;

    public ObracunZaradeService(ObracunZaradeRepository obracunZaradeRepository,
                                PlataRepository plataRepository,
                                ObracunZaposleniRepository obracunZaposleniRepository) {
        this.obracunZaradeRepository = obracunZaradeRepository;
        this.plataRepository = plataRepository;
        this.obracunZaposleniRepository = obracunZaposleniRepository;
    }

    public void makeObracunZarade(Date dateTime) {
        List<Plata> plate = plataRepository.findPlataByDatumAndStatusZaposlenog(dateTime, StatusZaposlenog.ZAPOSLEN);
        List<ObracunZaposleni> obracunZaposleni = obracunZaposleniRepository.findByStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        Map<ObracunZaposleni, Plata> join = new HashMap<>();
        for (Plata plata : plate) {
            Optional<ObracunZaposleni> obracunOptional = obracunZaposleni.stream()
                    .filter(oz -> oz.getZaposleni().getZaposleniId().equals(plata.getZaposleni().getZaposleniId()))
                    .findFirst();
            obracunOptional.ifPresent(zaposleni -> join.put(zaposleni, plata));
        }
        for (Map.Entry<ObracunZaposleni, Plata> entry : join.entrySet()) {
            obracunZaradeRepository.save(makeObracunZaradeObject(entry.getKey(), entry.getValue(), dateTime));
        }
    }

    public ObracunZarade updateObracunZaradeNaziv(Long id, String naziv) {
        Optional<ObracunZarade> obracunZaradeOptional = obracunZaradeRepository.findById(id);
        if (obracunZaradeOptional.isPresent()) {
            ObracunZarade obracunZarade = obracunZaradeOptional.get();
            obracunZarade.setNaziv(naziv);
            obracunZaradeRepository.save(obracunZarade);
            return obracunZarade;
        }
        throw new EntityNotFoundException();
    }

    private ObracunZarade makeObracunZaradeObject(ObracunZaposleni obracunZaposleni, Plata plata, Date dateTime) {
        ObracunZarade obracunZarade = new ObracunZarade();
        obracunZarade.setNaziv(new SimpleDateFormat("MM/yy").format(dateTime));
        obracunZarade.setDatumObracuna(dateTime);
        obracunZarade.setPorez(plata.getPorez());
        obracunZarade.setDoprinos1(plata.getDoprinos1());
        obracunZarade.setDoprinos2(plata.getDoprinos2());
        obracunZarade.setBrutoPlata(plata.getBrutoPlata());
        obracunZarade.setUkupanTrosakZarade(plata.getUkupanTrosakZarade());
        obracunZarade.setDatumOd(plata.getDatumOd());
        obracunZarade.setDatumDo(plata.getDatumDo());
        obracunZarade.setKomentar(plata.getKomentar());
        obracunZarade.setUcinak(obracunZaposleni.getUcinak());
        obracunZarade.setZaposleni(obracunZaposleni.getZaposleni());
        return obracunZarade;
    }

    public List<ObracunZarade> findByDate(Date date) {
        return obracunZaradeRepository.findAllByDate(date);
    }

    @Override
    public <S extends ObracunZarade> S save(S var1) {
        return obracunZaradeRepository.save(var1);
    }

    @Override
    public Optional<ObracunZarade> findById(Long var1) {
        if (obracunZaradeRepository.findById(var1).isPresent())
            return obracunZaradeRepository.findById(var1);
        throw new EntityNotFoundException();
    }

    @Override
    public List<ObracunZarade> findAll() {
        return obracunZaradeRepository.findAll();
    }

    @Override
    public void deleteById(Long var1) {
        //Nije specificirano.
    }
}
