package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Preduzece;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreduzeceRepository extends JpaRepository<Preduzece, Long> {
    List<Preduzece> findAll();

    Optional<Preduzece> findByPreduzeceId(Long preduzeceId);
}
