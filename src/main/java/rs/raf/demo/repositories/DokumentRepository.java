package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Dokument;
import rs.raf.demo.model.Faktura;

import java.util.Optional;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {

    public Optional<Dokument> findByBrojDokumenta(String brojDokumenta);
}
