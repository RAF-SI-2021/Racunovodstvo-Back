package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.DnevnikKnjizenja;

@Repository
public interface DnevnikKnjizenjaRepository extends JpaRepository<DnevnikKnjizenja, Long> {
}
