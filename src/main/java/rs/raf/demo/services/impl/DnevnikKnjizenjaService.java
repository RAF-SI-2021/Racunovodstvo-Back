package rs.raf.demo.services.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.DnevnikKnjizenja;
import rs.raf.demo.model.Faktura;
import rs.raf.demo.repositories.DnevnikKnjizenjaRepository;
import rs.raf.demo.services.IService;

import javax.validation.constraints.Null;
import java.util.*;

@Service
public class DnevnikKnjizenjaService implements IService<DnevnikKnjizenja, Long> {

    private final DnevnikKnjizenjaRepository dnevnikKnjizenjaRepository;

    public DnevnikKnjizenjaService(DnevnikKnjizenjaRepository dnevnikKnjizenjaRepository) {
        this.dnevnikKnjizenjaRepository = dnevnikKnjizenjaRepository;
    }



    public DnevnikKnjizenja save(DnevnikKnjizenja dnevnikKnjizenja){
        return dnevnikKnjizenjaRepository.save(dnevnikKnjizenja);
    }

    public Optional<DnevnikKnjizenja> findById(Long id) {
        return dnevnikKnjizenjaRepository.findByDnevnikKnjizenjaId(id);
    }

    public List<DnevnikKnjizenja> findAll() {return dnevnikKnjizenjaRepository.findAll();}

    public List<DnevnikKnjizenja> findAll(Specification<DnevnikKnjizenja> spec){
        return dnevnikKnjizenjaRepository.findAll(spec);
    }

    @Override
    public void deleteById(Long id) {
        dnevnikKnjizenjaRepository.deleteById(id);
    }


}
