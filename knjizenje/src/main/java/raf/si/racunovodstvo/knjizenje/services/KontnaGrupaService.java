package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;
import raf.si.racunovodstvo.knjizenje.repositories.KontnaGrupaRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.IKontnaGrupaService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class KontnaGrupaService implements IKontnaGrupaService {

    private KontnaGrupaRepository kontnaGrupaRepository;

    @Autowired
    public KontnaGrupaService(KontnaGrupaRepository kontnaGrupaRepository) {
        this.kontnaGrupaRepository = kontnaGrupaRepository;
    }

    @Override
    public KontnaGrupa save(KontnaGrupa kontnaGrupa) {
        return kontnaGrupaRepository.save(kontnaGrupa);
    }

    @Override
    public Optional<KontnaGrupa> findById(Long id) {
        return kontnaGrupaRepository.findById(id);
    }


    @Override
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
    public void deleteById(Long id) {
        kontnaGrupaRepository.deleteById(id);
    }
}
