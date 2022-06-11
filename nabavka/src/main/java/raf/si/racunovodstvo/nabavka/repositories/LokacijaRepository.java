package raf.si.racunovodstvo.nabavka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.nabavka.model.Lokacija;

import java.util.Optional;

@Repository
public interface LokacijaRepository extends JpaRepository<Lokacija, Long> {

    Optional<Lokacija> findByLokacijaId(Long lokacijaId);

}
