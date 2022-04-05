package rs.raf.demo.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Knjizenje;
import rs.raf.demo.model.Konto;
import rs.raf.demo.repositories.KnjizenjeRepository;
import rs.raf.demo.services.IKnjizenjeService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class KnjizenjeService implements IKnjizenjeService {

    private final KnjizenjeRepository knjizenjeRepository;

    public KnjizenjeService(KnjizenjeRepository knjizenjeRepository) {
        this.knjizenjeRepository = knjizenjeRepository;
    }

    @Override
    public <S extends Knjizenje> S save(S knjizenje) {
        return knjizenjeRepository.save(knjizenje);
    }

    @Override
    public Optional<Knjizenje> findById(Long id) {
        return knjizenjeRepository.findById(id);
    }

    @Override
    public List<Knjizenje> findAll() {
        return knjizenjeRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        knjizenjeRepository.deleteById(id);
    }

    @Override
    public Page<Knjizenje> findAll(Specification<Knjizenje> spec, Pageable pageSort) {
        return knjizenjeRepository.findAll(spec, pageSort);
    }

    @Override
    public Double getSumaPotrazujeZaKnjizenje(Long id) {
        Optional<Knjizenje> optionalKnjizenje = findById(id);
        if (optionalKnjizenje.isPresent()) {
            List<Konto> allKonto = optionalKnjizenje.get().getKonto();
            return allKonto.stream()
                           .map(Konto::getPotrazuje)
                           .filter(Objects::nonNull)
                           .mapToDouble(d -> d)
                           .sum();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Double getSumaDugujeZaKnjizenje(Long id) {
        Optional<Knjizenje> optionalKnjizenje = findById(id);
        if (optionalKnjizenje.isPresent()) {
            List<Konto> allKonto = optionalKnjizenje.get().getKonto();
            return allKonto.stream()
                           .map(Konto::getDuguje)
                           .filter(Objects::nonNull)
                           .mapToDouble(d -> d)
                           .sum();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Double getSaldoZaKnjizenje(Long id) {
        return this.getSumaPotrazujeZaKnjizenje(id) - this.getSumaDugujeZaKnjizenje(id);
    }

}
