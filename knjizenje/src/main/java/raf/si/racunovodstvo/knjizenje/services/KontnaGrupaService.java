package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.constants.RedisConstants;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;
import raf.si.racunovodstvo.knjizenje.repositories.KontnaGrupaRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.IKontnaGrupaService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class KontnaGrupaService implements IKontnaGrupaService {

    private final KontnaGrupaRepository kontnaGrupaRepository;

    @Autowired
    public KontnaGrupaService(KontnaGrupaRepository kontnaGrupaRepository) {
        this.kontnaGrupaRepository = kontnaGrupaRepository;
    }

    @Override
    @CachePut(value = RedisConstants.KONTNA_GRUPA_CACHE, key = "#result.kontnaGrupaId")
    public KontnaGrupa save(KontnaGrupa kontnaGrupa) {
        return kontnaGrupaRepository.save(kontnaGrupa);
    }

    @Override
    @Cacheable(value = RedisConstants.KONTNA_GRUPA_CACHE, key = "#id")
    public Optional<KontnaGrupa> findById(Long id) {
        return kontnaGrupaRepository.findById(id);
    }

    @Override
    @Cacheable(value = RedisConstants.KONTNA_GRUPA_CACHE, key = "#id")
    public KontnaGrupa findKontnaGrupaById(Long id) {
        return findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<KontnaGrupa> findAll() {return kontnaGrupaRepository.findAll(); }

    @Override
    public Page<KontnaGrupa> findAll(Pageable sort) {
        return kontnaGrupaRepository.findAll(sort);
    }

    @Override
    @CacheEvict(value = RedisConstants.KONTNA_GRUPA_CACHE, key = "#id")
    public void deleteById(Long id) {
        kontnaGrupaRepository.deleteById(id);
    }
}
