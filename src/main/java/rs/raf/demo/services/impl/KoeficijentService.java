package rs.raf.demo.services.impl;

import org.springframework.stereotype.Service;
import rs.raf.demo.model.Koeficijent;
import rs.raf.demo.repositories.KoeficijentRepository;
import rs.raf.demo.services.IService;

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
