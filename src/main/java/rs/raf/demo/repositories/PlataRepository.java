package rs.raf.demo.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Plata;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlataRepository extends JpaRepository<Plata, Long> {

    public List<Plata> findByZaposleniZaposleniId(Long zaposleniId);

    public Optional<Plata> findByPlataId(Long plataId);

    public List<Plata> findAll(Specification<Plata> spec);
}
