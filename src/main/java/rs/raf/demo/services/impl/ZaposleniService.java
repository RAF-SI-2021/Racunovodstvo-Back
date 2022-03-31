package rs.raf.demo.services.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Zaposleni;
import rs.raf.demo.repositories.ZaposleniRepository;
import rs.raf.demo.services.IZaposleniService;

import java.util.List;
import java.util.Optional;

@Service
public class ZaposleniService implements IZaposleniService {

    private final ZaposleniRepository zaposleniRepository;

    public ZaposleniService(ZaposleniRepository zaposleniRepository) {
        this.zaposleniRepository = zaposleniRepository;
    }

    @Override
    public <S extends Zaposleni> S save(S zaposleni) {
        return zaposleniRepository.save(zaposleni);
    }

    @Override
    public Optional<Zaposleni> findById(Long id) {
        return zaposleniRepository.findById(id);
    }

    @Override
    public List<Zaposleni> findAll() {
        return zaposleniRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        zaposleniRepository.deleteById(id);
    }

    @Override
    public List<Zaposleni> findAll(Specification<Zaposleni> spec) {
        return zaposleniRepository.findAll(spec);
    }
}
