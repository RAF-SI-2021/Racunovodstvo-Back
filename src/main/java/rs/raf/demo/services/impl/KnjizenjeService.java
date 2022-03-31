package rs.raf.demo.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Knjizenje;

import rs.raf.demo.model.enums.TipDokumenta;
import rs.raf.demo.repositories.KnjizenjeRepository;
import rs.raf.demo.services.IKnjizenjeService;



import java.util.List;
import java.util.Optional;

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
    public List<Knjizenje> findAll(Specification<Knjizenje> spec) {
        return knjizenjeRepository.findAll(spec);
    }

    @Override
    public List<Knjizenje> findByTipDokumenta(TipDokumenta tipDokumenta) {
        return null;
    }

}
