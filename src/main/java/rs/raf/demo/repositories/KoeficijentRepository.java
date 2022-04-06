package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Koeficijent;

@Repository
public interface KoeficijentRepository extends JpaRepository<Koeficijent, Long> {
    public Koeficijent findTopByOrderByDateDesc();
}
