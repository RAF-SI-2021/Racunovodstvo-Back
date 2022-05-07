package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;


@Repository
public interface KnjizenjeRepository extends JpaRepository<Knjizenje, Long> {

    Page<Knjizenje> findAll(Specification<Knjizenje> spec, Pageable pageSort);
    Page<Knjizenje> findAllKnjizenjaByKontoKontnaGrupa(Specification<Knjizenje> spec, Pageable pageSort, KontnaGrupa kontnaGrupa);
}