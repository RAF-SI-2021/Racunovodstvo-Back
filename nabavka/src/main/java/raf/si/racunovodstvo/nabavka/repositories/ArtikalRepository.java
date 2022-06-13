package raf.si.racunovodstvo.nabavka.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import raf.si.racunovodstvo.nabavka.model.Artikal;

public interface ArtikalRepository extends JpaRepository<Artikal, Long> {

    @Query("select ar from artikal ar where ar.baznaKonverzijaKalkulacija.id = :id")
    Page<Artikal> findAllByBaznaKonverzijaKalkulacijaId(Pageable pageable, Long id);

    Page<Artikal> findAll(Specification<Artikal> spec, Pageable pageSort);
}
