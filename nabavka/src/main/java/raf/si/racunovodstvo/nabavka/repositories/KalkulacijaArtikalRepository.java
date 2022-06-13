package raf.si.racunovodstvo.nabavka.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.model.KalkulacijaArtikal;

public interface KalkulacijaArtikalRepository extends JpaRepository<KalkulacijaArtikal, Long> {

    Page<Artikal> findAll(Specification<Artikal> spec, Pageable pageSort);

}
