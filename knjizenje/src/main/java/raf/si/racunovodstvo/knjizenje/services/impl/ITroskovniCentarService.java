package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;
import raf.si.racunovodstvo.knjizenje.responses.TroskovniCentarResponse;

import java.util.List;
import java.util.Optional;

public interface ITroskovniCentarService extends IService<TroskovniCentar, Long> {

    Page<TroskovniCentar> findAll(Pageable sort);
    TroskovniCentar updateTroskovniCentar(TroskovniCentar troskovniCentar);
    TroskovniCentar addKontosFromKnjizenje(Knjizenje knjizenje, TroskovniCentar troskovniCentar);
    List<TroskovniCentarResponse> findAllTroskovniCentriResponse();
    void deleteBazniKontoById(Long bazniKontoId);
    Optional<BazniKonto> findBazniKontoById(Long bazniKontoId);
}
