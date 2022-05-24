package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.model.Dokument;
import raf.si.racunovodstvo.knjizenje.repositories.DokumentRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.IService;

import java.util.List;
import java.util.Optional;

@Service
public class DokumentService implements IService<Dokument, Long> {

    private final DokumentRepository dokumentRepository;

    @Autowired
    public DokumentService(DokumentRepository dokumentRepository) {
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
}
