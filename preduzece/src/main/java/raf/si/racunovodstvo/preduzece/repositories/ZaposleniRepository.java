package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;

import java.util.List;

@Repository
public interface ZaposleniRepository extends JpaRepository<Zaposleni, Long> {
    List<Zaposleni> findAll(Specification<Zaposleni> spec);

}
