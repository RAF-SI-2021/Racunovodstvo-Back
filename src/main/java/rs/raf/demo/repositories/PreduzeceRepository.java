package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.demo.model.Preduzece;

import java.util.List;
import java.util.Optional;

public interface PreduzeceRepository extends JpaRepository<Preduzece, Long> {
    public List<Preduzece> findAll();

    public Optional<Preduzece> findByPreduzeceId(Long preduzeceId);
}