package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Dokument;
import rs.raf.demo.model.Knjizenje;
import rs.raf.demo.model.Konto;
import rs.raf.demo.repositories.DokumentRepository;
import rs.raf.demo.services.IDokumentService;

import java.util.List;
import java.util.Optional;

@Service
public class DokumentService implements IDokumentService {

    private final DokumentRepository dokumentRepository;

    @Autowired
    public DokumentService(DokumentRepository dokumentRepository) {
        this.dokumentRepository = dokumentRepository;
    }

    @Override
    public <S extends Dokument> S save(S var1) {
        return dokumentRepository.save(var1);
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
    public void deleteById(Long var1) {

    }

    @Override
    public Double getSumaPotrazuje(Long id) {
        Optional<Dokument> dok = findById(id);
        if (dok.isPresent()) {
            List<Knjizenje> knjizenja = dok.get().getKnjizenje();
            Double suma = (double) 0;
            for (Knjizenje i : knjizenja) {
                List<Konto> allKonto = i.getKonto();
                for (Konto j : allKonto) {
                    suma += j.getPotrazuje();
                }
            }
            return suma;
        }
        else
            return null;
    }

    @Override
    public Double getSumaDuguje(Long id) {
        Optional<Dokument> dok = findById(id);
        if (dok.isPresent()) {
            List<Knjizenje> knjizenja = dok.get().getKnjizenje();
            Double suma = (double) 0;
            for (Knjizenje i : knjizenja) {
                List<Konto> allKonto = i.getKonto();
                for (Konto j : allKonto) {
                    suma += j.getDuguje();
                }
            }
            return suma;
        }
        else
            return null;
    }

    @Override
    public Double getSaldo(Long id) {
        return getSumaPotrazuje(id) - getSumaDuguje(id);
    }
}
