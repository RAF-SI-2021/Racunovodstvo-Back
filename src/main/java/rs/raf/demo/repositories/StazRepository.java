package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Staz;

@Repository
public interface StazRepository extends JpaRepository<Staz, Long> {
}
