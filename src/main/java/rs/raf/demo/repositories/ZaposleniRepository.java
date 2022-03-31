package rs.raf.demo.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Zaposleni;

import java.util.List;

@Repository
public interface ZaposleniRepository extends JpaRepository<Zaposleni, Long> {

    List<Zaposleni> findAll(Specification<Zaposleni> spec);
}
