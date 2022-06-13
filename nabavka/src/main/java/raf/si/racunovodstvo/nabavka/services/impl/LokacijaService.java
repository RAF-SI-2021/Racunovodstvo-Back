package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.constants.RedisConstants;
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
    @CachePut(value = RedisConstants.LOKACIJA_CACHE, key = "#result.lokacijaId")
    public Lokacija save(Lokacija lokacija) {
        return lokacijaRepository.save(lokacija);
    }

    @Override
    @CachePut(value = RedisConstants.LOKACIJA_CACHE, key = "#result.lokacijaId")
    public Lokacija update(Lokacija lokacija) {
        if (findById(lokacija.getLokacijaId()).isPresent()){
            return lokacijaRepository.save(lokacija);
        }
        throw new EntityNotFoundException();
    }

    @Override
    @Cacheable(value = RedisConstants.LOKACIJA_CACHE, key = "#id")
    public Optional<Lokacija> findById(Long id) {
        return lokacijaRepository.findByLokacijaId(id);
    }

    @Override
    public List<Lokacija> findAll() {
        return lokacijaRepository.findAll();
    }

    @Override
    @CacheEvict(value = RedisConstants.LOKACIJA_CACHE, key = "#id")
    public void deleteById(Long id) {
        Optional<Lokacija> optionalLokacija = findById(id);
        if (optionalLokacija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        lokacijaRepository.deleteById(id);
    }
}
