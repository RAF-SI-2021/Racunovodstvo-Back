package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;

import java.util.List;

@Repository
public interface ProfitniCentarRepository extends JpaRepository<ProfitniCentar, Long> {

    List<ProfitniCentar> findProfitniCentarByParentProfitniCentar(ProfitniCentar profitniCentar);
}
