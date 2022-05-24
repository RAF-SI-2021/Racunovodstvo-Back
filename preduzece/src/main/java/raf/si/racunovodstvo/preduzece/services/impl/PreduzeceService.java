package raf.si.racunovodstvo.preduzece.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.model.Preduzece;
import raf.si.racunovodstvo.preduzece.repositories.PreduzeceRepository;
import raf.si.racunovodstvo.preduzece.services.IService;
import raf.si.racunovodstvo.preduzece.utils.SearchUtil;

import java.util.List;
import java.util.Optional;

@Service
public class PreduzeceService implements IService<Preduzece, Long> {

    private final PreduzeceRepository preduzeceRepository;

    private final SearchUtil<Preduzece> searchUtil;

    @Autowired
    public PreduzeceService(PreduzeceRepository preduzeceRepository) {
        this.preduzeceRepository = preduzeceRepository;
        this.searchUtil = new SearchUtil<>();
    }

    @Override
    public Preduzece save(Preduzece preduzece) {
        preduzece.setIsActive(true);
        return preduzeceRepository.save(preduzece);
    }

    @Override
    public Optional<Preduzece> findById(Long id) {
        return preduzeceRepository.findByPreduzeceId(id);
    }

    @Override
    public List<Preduzece> findAll() {
        Specification<Preduzece> spec = this.searchUtil.getSpec("isActive:true");
        return preduzeceRepository.findAll(spec);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Preduzece> optionalPreduzece = preduzeceRepository.findByPreduzeceId(id);
        if (optionalPreduzece.isPresent()) {
            Preduzece preduzece = optionalPreduzece.get();
            preduzece.setIsActive(false);
            preduzeceRepository.save(preduzece);
        }
    }
}
