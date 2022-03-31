package rs.raf.demo.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Knjizenje;

import java.util.List;


@Repository
public interface KnjizenjeRepository extends JpaRepository<Knjizenje, Long> {

    List<Knjizenje> findAll(Specification<Knjizenje> spec);
}