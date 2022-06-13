package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.constants.RedisConstants;
import raf.si.racunovodstvo.knjizenje.model.Dokument;
import raf.si.racunovodstvo.knjizenje.repositories.DokumentRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.IService;

import java.util.List;
import java.util.Optional;

@Service
public class DokumentService implements IService<Dokument, Long> {

    private final DokumentRepository dokumentRepository;

    @Autowired
    public DokumentService(DokumentRepository dokumentRepository) {
        this.dokumentRepository = dokumentRepository;
    }

    @Override
    @CachePut(value = RedisConstants.DOKUMENT_CACHE, key = "#result.dokumentId")
    public Dokument save(Dokument dokument) {
        return dokumentRepository.save(dokument);
    }

    @Override
    @Cacheable(value = RedisConstants.DOKUMENT_CACHE, key = "#id")
    public Optional<Dokument> findById(Long id) {
        return dokumentRepository.findById(id);
    }

    @Override
    public List<Dokument> findAll() {
        return dokumentRepository.findAll();
    }

    @Override
    @CacheEvict(value = RedisConstants.DOKUMENT_CACHE, key = "#id")
    public void deleteById(Long id) {
        dokumentRepository.deleteById(id);
    }
}
