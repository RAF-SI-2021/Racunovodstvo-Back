package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.racunovodstvo.preduzece.model.Obracun;


public interface ObracunRepository extends JpaRepository<Obracun, Long> {
}