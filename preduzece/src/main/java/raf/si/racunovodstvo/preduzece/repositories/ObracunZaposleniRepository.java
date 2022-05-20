package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;


public interface ObracunZaposleniRepository extends JpaRepository<ObracunZaposleni, Long> {

    ObracunZaposleni findByZaposleniAndObracun(Zaposleni zaposleni, Obracun obracun);
}
