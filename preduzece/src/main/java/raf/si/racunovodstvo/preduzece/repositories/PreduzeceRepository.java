package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.preduzece.model.Preduzece;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreduzeceRepository extends JpaRepository<Preduzece, Long> {
    List<Preduzece> findAll();

    Optional<Preduzece> findByPreduzeceId(Long preduzeceId);

    List<Preduzece> findAll(Specification<Preduzece> spec);
}
