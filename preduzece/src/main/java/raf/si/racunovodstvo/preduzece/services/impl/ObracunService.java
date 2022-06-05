package raf.si.racunovodstvo.preduzece.services.impl;

import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.repositories.ObracunRepository;
import raf.si.racunovodstvo.preduzece.services.IObracunService;
import java.util.List;
import java.util.Optional;

@Service
public class ObracunService implements IObracunService {

    private final ObracunRepository obracunRepository;

    public ObracunService(ObracunRepository obracunRepository){
        this.obracunRepository = obracunRepository;
    }

    @Override
    public <S extends Obracun> S save(S var1) {
        return obracunRepository.save(var1);
    }

    @Override
    public Optional<Obracun> findById(Long var1) {
        return obracunRepository.findById(var1);
    }

    @Override
    public List<Obracun> findAll() {
        return obracunRepository.findAll();
    }

    @Override
    public void deleteById(Long var1) {
        obracunRepository.deleteById(var1);
    }


    public void updateObracunZaradeNaziv(Long obracunZaradeId, String naziv) {
        Optional<Obracun> obracunOptional = obracunRepository.findById(obracunZaradeId);
        if (obracunOptional.isPresent()) {
            Obracun obracun = obracunOptional.get();
            obracun.setNaziv(naziv);
            save(obracun);
        }
    }
}
