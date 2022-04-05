package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Dokument;
import rs.raf.demo.model.Knjizenje;
import rs.raf.demo.repositories.DokumentRepository;
import rs.raf.demo.services.IDokumentService;
import rs.raf.demo.services.IKnjizenjeService;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class DokumentService implements IDokumentService {

    private final IKnjizenjeService knjizenjeService;

    private final DokumentRepository dokumentRepository;

    @Autowired
    public DokumentService(KnjizenjeService knjizenjeService, DokumentRepository dokumentRepository) {
        this.knjizenjeService = knjizenjeService;
        this.dokumentRepository = dokumentRepository;
    }

    @Override
    public Dokument save(Dokument id) {
        return dokumentRepository.save(id);
    }

    @Override
    public Optional<Dokument> findById(Long id) {
        return dokumentRepository.findById(id);
    }

    @Override
    public List<Dokument> findAll() {
        return dokumentRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        dokumentRepository.deleteById(id);
    }

    @Override
    public Double getSumaPotrazuje(Long id) {
        Optional<Dokument> dok = findById(id);
        if (dok.isPresent()) {
            List<Knjizenje> knjizenja = dok.get().getKnjizenje();
            return knjizenja.stream()
                            .mapToDouble(knjizenje -> knjizenjeService.getSumaPotrazujeZaKnjizenje(knjizenje.getKnjizenjeId()))
                            .sum();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Double getSumaDuguje(Long id) {
        Optional<Dokument> dok = findById(id);
        if (dok.isPresent()) {
            List<Knjizenje> knjizenja = dok.get().getKnjizenje();
            return knjizenja.stream()
                            .mapToDouble(knjizenje -> knjizenjeService.getSumaDugujeZaKnjizenje(knjizenje.getKnjizenjeId()))
                            .sum();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Double getSaldo(Long id) {
        return getSumaPotrazuje(id) - getSumaDuguje(id);
    }
}
