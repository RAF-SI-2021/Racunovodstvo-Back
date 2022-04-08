package rs.raf.demo.services.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Plata;
import rs.raf.demo.model.Zaposleni;
import rs.raf.demo.repositories.PlataRepository;
import rs.raf.demo.requests.PlataRequest;
import rs.raf.demo.services.IService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class PlataService implements IService<Plata, Long> {
    private final PlataRepository platarepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final KoeficijentService koeficijentService;

    public PlataService(PlataRepository plataRepository, KoeficijentService koeficijentService) {
        this.platarepository = plataRepository;
        this.koeficijentService = koeficijentService;
    }

    @Override
    public <S extends Plata> S save(S var1) {
        return this.platarepository.save(var1);
    }

    public Plata save(PlataRequest plataRequest) {
        Plata plata = new Plata();
        plata.setNetoPlata(plataRequest.getNetoPlata());
        plata.setDatum(plataRequest.getDatum());
        plata.setZaposleni(this.entityManager.getReference(Zaposleni.class, plataRequest.getZaposleniId()));
        plata.izracunajDoprinose(this.koeficijentService.getCurrentKoeficijent());
        return this.platarepository.save(plata);
    }

    @Override
    public Optional<Plata> findById(Long var1) {
        return this.platarepository.findByPlataId(var1);
    }

    @Override
    public List<Plata> findAll() {
        return this.platarepository.findAll();
    }

    public List<Plata> findAll(Specification<Plata> spec) {
        return this.platarepository.findAll(spec);
    }

    @Override
    public void deleteById(Long var1) {
        this.platarepository.deleteById(var1);
    }

    public List<Plata> findByZaposleniZaposleniId(Long zaposleniId) {
        return this.platarepository.findByZaposleniZaposleniId(zaposleniId);
    }
}
