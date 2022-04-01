package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Konto;
import rs.raf.demo.repositories.KontoRepository;
import rs.raf.demo.services.IService;

import java.util.*;

@Service
public class KontoService implements IService<Konto, Long> {
    private final KontoRepository kontoRepository;

    @Autowired
    public KontoService(KontoRepository kontoRepository) {
        this.kontoRepository = kontoRepository;
    }

    @Override
    public Konto save(Konto konto) {
        return kontoRepository.save(konto);
    }

    @Override
    public Optional<Konto> findById(Long id) {
        return kontoRepository.findById(id);
    }

    public Konto update(Long id) {
        return save(findById(id).orElseThrow(NoSuchElementException::new));
    }

    @Override
    public List<Konto> findAll() {
        return kontoRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        kontoRepository.deleteById(id);
    }
}
