package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.KontnaGrupa;
import rs.raf.demo.repositories.KontnaGrupaRepository;
import rs.raf.demo.services.IKontnaGrupaService;

import java.util.*;


@Service
public class KontnaGrupaService implements IKontnaGrupaService {
    KontnaGrupaRepository kontnaGrupaRepository;

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
