package rs.raf.demo.services.impl;

import org.springframework.stereotype.Service;
import rs.raf.demo.model.Knjizenje;
import rs.raf.demo.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class KnjizenjeService implements IService<Knjizenje, Long> {

    @Override
    public <S extends Knjizenje> S save(S var1) {
        return null;
    }

    @Override
    public Optional<Knjizenje> findById(Long var1) {
        return Optional.empty();
    }

    @Override
    public List<Knjizenje> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long var1) {

    }
}
