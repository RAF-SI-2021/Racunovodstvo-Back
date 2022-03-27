package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Dokument;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {
}
