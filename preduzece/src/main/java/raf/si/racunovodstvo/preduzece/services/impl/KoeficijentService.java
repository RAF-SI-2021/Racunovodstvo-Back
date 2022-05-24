package raf.si.racunovodstvo.preduzece.services.impl;

import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.model.Koeficijent;
import raf.si.racunovodstvo.preduzece.repositories.KoeficijentRepository;
import raf.si.racunovodstvo.preduzece.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class KoeficijentService implements IService<Koeficijent, Long> {
    public final KoeficijentRepository koeficijentRepository;

    public KoeficijentService(KoeficijentRepository koeficijentRepository) {
        this.koeficijentRepository = koeficijentRepository;
    }

    @Override
    public <S extends Koeficijent> S save(S var1) {
        return this.koeficijentRepository.save(var1);
    }

    @Override
    public Optional<Koeficijent> findById(Long var1) {
        return this.koeficijentRepository.findById(var1);
    }

    @Override
    public List<Koeficijent> findAll() {
        return this.koeficijentRepository.findAll();
    }

    @Override
    public void deleteById(Long var1) {
        this.koeficijentRepository.deleteById(var1);
    }

    public Koeficijent getCurrentKoeficijent() {
        return this.koeficijentRepository.findTopByOrderByDateDesc();
    }
}
