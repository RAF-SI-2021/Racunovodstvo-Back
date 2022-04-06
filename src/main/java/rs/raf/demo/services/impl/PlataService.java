package rs.raf.demo.services.impl;

import org.springframework.stereotype.Service;
import rs.raf.demo.model.Plata;
import rs.raf.demo.repositories.PlataRepository;
import rs.raf.demo.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class PlataService implements IService<Plata, Long> {
    private final PlataRepository platarepository;

    public PlataService(PlataRepository plataRepository) {
        this.platarepository = plataRepository;
    }

    @Override
    public <S extends Plata> S save(S var1) {
        return this.platarepository.save(var1);
    }

    @Override
    public Optional<Plata> findById(Long var1) {
        return this.platarepository.findByPlataId(var1);
    }

    @Override
    public List<Plata> findAll() {
        return this.platarepository.findAll();
    }

    @Override
    public void deleteById(Long var1) {
        this.platarepository.deleteById(var1);
    }

    public List<Plata> findByZaposleniZaposleniId(Long zaposleniId) {
        return this.platarepository.findByZaposleniZaposleniId(zaposleniId);
    }
}
