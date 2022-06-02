package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;

import java.util.Optional;


public interface ObracunZaposleniRepository extends JpaRepository<ObracunZaposleni, Long> {

    Optional<ObracunZaposleni> findByZaposleniAndObracun(Zaposleni zaposleni, Obracun obracun);

    Page<ObracunZaposleni> findAll(Specification<ObracunZaposleni> spec, Pageable pageSort);
}
