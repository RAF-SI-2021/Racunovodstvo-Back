package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.Dokument;

import java.util.Optional;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {

    public Optional<Dokument> findByBrojDokumenta(String brojDokumenta);
}
