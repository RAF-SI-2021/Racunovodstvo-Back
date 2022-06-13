package raf.si.racunovodstvo.preduzece.services.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.constants.RedisConstants;
import raf.si.racunovodstvo.preduzece.model.Staz;
import raf.si.racunovodstvo.preduzece.repositories.StazRepository;
import raf.si.racunovodstvo.preduzece.services.IStazService;

import java.util.List;
import java.util.Optional;

@Service
public class StazService implements IStazService {

    private final StazRepository stazRepository;

    public StazService(StazRepository stazRepository) {
        this.stazRepository = stazRepository;
    }

    @Override
    @CachePut(value = RedisConstants.STAZ_CACHE, key = "#result.stazId")
    public <S extends Staz> S save(S staz) {
        return stazRepository.save(staz);
    }

    @Override
    @Cacheable(value = RedisConstants.STAZ_CACHE, key = "#id")
    public Optional<Staz> findById(Long id) {
        return stazRepository.findById(id);
    }

    @Override
    public List<Staz> findAll() {
        return stazRepository.findAll();
    }

    @Override
    @CacheEvict(value = RedisConstants.STAZ_CACHE, key = "#id")
    public void deleteById(Long id) {
        stazRepository.deleteById(id);
    }
}
