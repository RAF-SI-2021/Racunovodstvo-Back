package rs.raf.demo.services;

import org.springframework.stereotype.Service;
import rs.raf.demo.model.Faktura;
import rs.raf.demo.repositories.FakturaRepository;

import java.util.List;
import java.util.Optional;
@Service
public class FakturaService implements IService<Faktura, Long> {

    private final FakturaRepository fakturaRepository;

    public FakturaService(FakturaRepository fakturaRepository) {
        this.fakturaRepository = fakturaRepository;
    }

    @Override
    public <S extends Faktura> S save(S var1) {
        return null;
    }

    @Override
    public Optional<Faktura> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Faktura> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {fakturaRepository.deleteById(id);}
}
