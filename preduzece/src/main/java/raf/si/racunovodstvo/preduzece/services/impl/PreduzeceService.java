package raf.si.racunovodstvo.preduzece.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.constants.RedisConstants;
import raf.si.racunovodstvo.preduzece.model.Preduzece;
import raf.si.racunovodstvo.preduzece.repositories.PreduzeceRepository;
import raf.si.racunovodstvo.preduzece.responses.PreduzeceResponse;
import raf.si.racunovodstvo.preduzece.services.IPreduzeceService;
import raf.si.racunovodstvo.preduzece.utils.SearchUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PreduzeceService implements IPreduzeceService {

    private final PreduzeceRepository preduzeceRepository;

    private final ModelMapper modelMapper;

    private final SearchUtil<Preduzece> searchUtil;

    @Autowired
    public PreduzeceService(PreduzeceRepository preduzeceRepository, ModelMapper modelMapper) {
        this.preduzeceRepository = preduzeceRepository;
        this.modelMapper = modelMapper;
        this.searchUtil = new SearchUtil<>();
    }

    @Override
    @CachePut(value = RedisConstants.PREDUZECE_CACHE, key = "#preduzece.preduzeceId")
    public Preduzece save(Preduzece preduzece) {
        preduzece.setIsActive(true);
        return preduzeceRepository.save(preduzece);
    }

    @Override
    @Cacheable(value = RedisConstants.PREDUZECE_CACHE, key = "#id")
    public Optional<Preduzece> findById(Long id) {
        return preduzeceRepository.findByPreduzeceId(id);
    }

    @Override
    public List<Preduzece> findAll() {
        Specification<Preduzece> spec = this.searchUtil.getSpec("isActive:true");
        return preduzeceRepository.findAll(spec);
    }

    @Override
    @CacheEvict(value = RedisConstants.PREDUZECE_CACHE, key = "#id")
    public void deleteById(Long id) {
        Optional<Preduzece> optionalPreduzece = preduzeceRepository.findByPreduzeceId(id);
        if (optionalPreduzece.isPresent()) {
            Preduzece preduzece = optionalPreduzece.get();
            preduzece.setIsActive(false);
            preduzeceRepository.save(preduzece);
        }
    }

    @Override
    @CachePut(value = RedisConstants.PREDUZECE_CACHE, key = "#result.preduzeceId")
    public PreduzeceResponse savePreduzece(Preduzece preduzece) {
        return modelMapper.map(save(preduzece), PreduzeceResponse.class);
    }

    @Override
    @Cacheable(value = RedisConstants.PREDUZECE_CACHE, key = "#id")
    public Optional<PreduzeceResponse> findPreduzeceById(Long id) {
        return findById(id).map(preduzece -> modelMapper.map(preduzece, PreduzeceResponse.class));
    }

    @Override
    public List<PreduzeceResponse> findAllPreduzece() {
        return findAll().stream().map(preduzece -> modelMapper.map(preduzece, PreduzeceResponse.class)).collect(Collectors.toList());
    }
}
