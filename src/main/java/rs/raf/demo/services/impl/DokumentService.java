package rs.raf.demo.services.impl;

import org.springframework.stereotype.Service;
import rs.raf.demo.model.Dokument;
import rs.raf.demo.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class DokumentService implements IService<Dokument, Long> {
    @Override
    public <S extends Dokument> S save(S var1) {
        return null;
    }

    @Override
    public Optional<Dokument> findById(Long var1) {
        return Optional.empty();
    }

    @Override
    public List<Dokument> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long var1) {

    }
}
