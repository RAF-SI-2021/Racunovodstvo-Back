package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;

import java.util.List;

@Repository
public interface KontoRepository extends JpaRepository<Konto, Long> {

    public List<Konto> findAll();

    List<Konto> findKontoByKontnaGrupaBrojKonta(String kontnaGrupaId);

    List<Konto> findAll(Specification<Konto> spec);

    Page<Konto> findAll(Specification<Konto> spec, Pageable pageSort);

    List<Konto> findKontoByKnjizenje(Knjizenje knjizenje);

}
