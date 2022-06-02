package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;

import java.util.List;

public interface IProfitniCentarService extends IService<ProfitniCentar,Long> {

    Page<ProfitniCentar> findAll(Pageable sort);
    ProfitniCentar updateProfitniCentar(ProfitniCentar profitniCentar);

    ProfitniCentar addKontosFromKnjizenje(List<Konto> kontoList, ProfitniCentar profitniCentar);
}
