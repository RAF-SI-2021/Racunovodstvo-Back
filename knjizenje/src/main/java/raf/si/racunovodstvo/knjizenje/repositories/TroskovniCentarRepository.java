package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;

@Repository
public interface TroskovniCentarRepository extends JpaRepository<TroskovniCentar, Long> {
}
