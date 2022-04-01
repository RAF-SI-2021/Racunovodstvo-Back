package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.KontnaGrupa;

@Repository
public interface KontnaGrupaRepository extends JpaRepository<KontnaGrupa, String> {
}
