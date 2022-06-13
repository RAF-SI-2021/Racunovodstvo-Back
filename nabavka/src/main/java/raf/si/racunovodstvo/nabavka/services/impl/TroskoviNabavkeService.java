package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.constants.RedisConstants;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.repositories.TroskoviNabavkeRepository;
import raf.si.racunovodstvo.nabavka.services.ITroskoviNabavkeService;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class TroskoviNabavkeService implements ITroskoviNabavkeService {

    private final TroskoviNabavkeRepository troskoviNabavkeRepository;

    @Autowired
    public TroskoviNabavkeService(TroskoviNabavkeRepository troskoviNabavkeRepository) {
        this.troskoviNabavkeRepository = troskoviNabavkeRepository;
    }

    @Override
    @CachePut(value = RedisConstants.TROSKOVI_NABAVKE_CACHE, key = "#result.troskoviNabavkeId")
    public TroskoviNabavke save(TroskoviNabavke troskoviNabavke) {
        return troskoviNabavkeRepository.save(troskoviNabavke);
    }

    @Override
    @CachePut(value = RedisConstants.TROSKOVI_NABAVKE_CACHE, key = "#result.troskoviNabavkeId")
    public TroskoviNabavke update(TroskoviNabavke troskoviNabavke) {
        if (findById(troskoviNabavke.getTroskoviNabavkeId()).isPresent()) {
            return troskoviNabavkeRepository.save(troskoviNabavke);
        }
        throw new EntityNotFoundException();
    }

    @Override
    @Cacheable(value = RedisConstants.TROSKOVI_NABAVKE_CACHE, key = "#id")
    public Optional<TroskoviNabavke> findById(Long id) {
        return troskoviNabavkeRepository.findById(id);
    }

    @Override
    public List<TroskoviNabavke> findAll() {
        return troskoviNabavkeRepository.findAll();
    }

    @Override
    @CacheEvict(value = RedisConstants.TROSKOVI_NABAVKE_CACHE, key = "#id")
    public void deleteById(Long id) {
        Optional<TroskoviNabavke> optionalTroskoviNabavke = troskoviNabavkeRepository.findById(id);
        if (optionalTroskoviNabavke.isEmpty()) {
            throw new EntityNotFoundException();
        }
        troskoviNabavkeRepository.deleteById(id);
    }
}
