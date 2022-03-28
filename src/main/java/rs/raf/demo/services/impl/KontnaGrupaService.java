package rs.raf.demo.services.impl;

import org.springframework.stereotype.Service;
import rs.raf.demo.model.KontnaGrupa;
import rs.raf.demo.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class KontnaGrupaService implements IService<KontnaGrupa, Long> {
    @Override
    public <S extends KontnaGrupa> S save(S var1) {
        return null;
    }

    @Override
    public Optional<KontnaGrupa> findById(Long var1) {
        return Optional.empty();
    }

    @Override
    public List<KontnaGrupa> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long var1) {

    }
}
