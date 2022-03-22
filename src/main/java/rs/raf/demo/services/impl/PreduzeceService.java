package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Preduzece;
import rs.raf.demo.repositories.PreduzeceRepository;
import rs.raf.demo.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class PreduzeceService implements IService<Preduzece, Long> {

    private final PreduzeceRepository preduzeceRepository;

    @Autowired
    public PreduzeceService(PreduzeceRepository preduzeceRepository) {
        this.preduzeceRepository = preduzeceRepository;
    }

    @Override
    public Preduzece save(Preduzece preduzece) {
        return preduzeceRepository.save(preduzece);
    }

    @Override
    public Optional<Preduzece> findById(Long id) {
        return preduzeceRepository.findByPreduzeceId(id);
    }

    @Override
    public List<Preduzece> findAll() {
        return preduzeceRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        preduzeceRepository.deleteById(id);
    }
}
