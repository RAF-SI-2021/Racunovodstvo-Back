package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.model.Povracaj;
import raf.si.racunovodstvo.knjizenje.repositories.PovracajRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.IPovracajService;

import java.util.List;
import java.util.Optional;

@Service
public class PovracajService implements IPovracajService {

    private final PovracajRepository povracajRepository;

    @Autowired
    public PovracajService(PovracajRepository povracajRepository) {
        this.povracajRepository = povracajRepository;
    }

    public List<Povracaj> findAll(){
        return povracajRepository.findAll();
    }

    @Override
    public Page<Povracaj> findAll(Pageable pageSort) {
        return povracajRepository.findAll(pageSort);
    }

    public Optional<Povracaj> findById(Long id){
        return povracajRepository.findById(id);
    }

    public Povracaj save(Povracaj povracaj){
        return this.povracajRepository.save(povracaj);
    }

    public void deleteById(Long id) {
        povracajRepository.deleteById(id);
    }
}
