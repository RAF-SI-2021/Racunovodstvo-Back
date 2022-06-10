package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.model.Lokacija;
import raf.si.racunovodstvo.nabavka.repositories.LokacijaRepository;
import raf.si.racunovodstvo.nabavka.services.ILokacijaService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class LokacijaService implements ILokacijaService {

    private final LokacijaRepository lokacijaRepository;

    @Autowired
    public LokacijaService(LokacijaRepository lokacijaRepository){
        this.lokacijaRepository = lokacijaRepository;
    }

    @Override
    public Lokacija save(Lokacija lokacija) {
        return lokacijaRepository.save(lokacija);
    }

    @Override
    public Lokacija update(Lokacija lokacija) {
        if (lokacijaRepository.findByLokacijaId(lokacija.getLokacijaId()).isPresent()){
            return lokacijaRepository.save(lokacija);
        }
        throw new EntityNotFoundException();
    }

    @Override
    public Optional<Lokacija> findById(Long id) {
        return lokacijaRepository.findByLokacijaId(id);
    }

    @Override
    public List<Lokacija> findAll() {
        return lokacijaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<Lokacija> optionalLokacija = lokacijaRepository.findByLokacijaId(id);
        if (optionalLokacija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        lokacijaRepository.deleteById(id);
    }
}
