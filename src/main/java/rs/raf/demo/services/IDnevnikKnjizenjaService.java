package rs.raf.demo.services;

import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.model.DnevnikKnjizenja;


import java.util.List;


public interface IDnevnikKnjizenjaService extends IService<DnevnikKnjizenja, Long>{
    List<DnevnikKnjizenja> findAll(Specification<DnevnikKnjizenja> spec);
}
