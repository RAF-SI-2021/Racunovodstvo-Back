package rs.raf.demo.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Faktura;
import rs.raf.demo.model.KontnaGrupa;
import rs.raf.demo.model.Konto;

import java.util.List;
import java.util.Optional;

@Repository
public interface KontoRepository extends JpaRepository<Konto, Long> {

    public List<Konto> findAll();

    List<Konto> findKontoByKontnaGrupaBrojKonta(String kontnaGrupaId);

    List<Konto> findAll(Specification<Konto> spec);

}
