package rs.raf.demo.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.DnevnikKnjizenja;


import java.util.List;
import java.util.Optional;

@Repository
public interface DnevnikKnjizenjaRepository extends JpaRepository<DnevnikKnjizenja, Long> {


    List<DnevnikKnjizenja> findAll(Specification<DnevnikKnjizenja> spec);

    public Optional<DnevnikKnjizenja> findByDnevnikKnjizenjaId(Long dnevnikKnjizenjaId);

}
