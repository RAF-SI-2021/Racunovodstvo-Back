package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.preduzece.model.Staz;

@Repository
public interface StazRepository extends JpaRepository<Staz, Long> {
}
