package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;

public interface ITroskovniCentarService extends IService<TroskovniCentar, Long> {

    Page<TroskovniCentar> findAll(Pageable sort);

    TroskovniCentar updateTroskovniCentar(TroskovniCentar troskovniCentar);

    TroskovniCentar addKontosFromKnjizenje(Knjizenje knjizenje, TroskovniCentar troskovniCentar);
}
