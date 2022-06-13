package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.preduzece.model.Koeficijent;

@Repository
public interface KoeficijentRepository extends JpaRepository<Koeficijent, Long> {
    Koeficijent findFirstByOrderByDateDesc();
}
