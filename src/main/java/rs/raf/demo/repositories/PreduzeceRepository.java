package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Preduzece;

@Repository
public interface PreduzeceRepository extends JpaRepository<Preduzece, Long> {
}
