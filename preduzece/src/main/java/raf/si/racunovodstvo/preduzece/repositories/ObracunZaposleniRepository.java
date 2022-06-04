package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;

import java.util.List;
import java.util.Optional;


public interface ObracunZaposleniRepository extends JpaRepository<ObracunZaposleni, Long> {

    Optional<ObracunZaposleni> findByZaposleniAndObracun(Zaposleni zaposleni, Obracun obracun);

    Page<ObracunZaposleni> findAll(Specification<ObracunZaposleni> spec, Pageable pageSort);

    @Query("select oz from ObracunZaposleni oz where oz.zaposleni.statusZaposlenog = :statusZaposlenog")
    List<ObracunZaposleni> findByStatusZaposlenog(StatusZaposlenog statusZaposlenog);
}
