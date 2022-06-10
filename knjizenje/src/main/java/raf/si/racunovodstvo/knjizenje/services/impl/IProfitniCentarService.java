package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.requests.ProfitniCentarRequest;
import raf.si.racunovodstvo.knjizenje.responses.ProfitniCentarResponse;

import java.util.List;
import java.util.Optional;

public interface IProfitniCentarService extends IService<ProfitniCentar,Long> {

    Page<ProfitniCentar> findAll(Pageable sort);
    ProfitniCentar updateProfitniCentar(ProfitniCentarRequest profitniCentar);
    ProfitniCentar addKontosFromKnjizenje(Knjizenje knjizenje, ProfitniCentar profitniCentar);
    List<ProfitniCentarResponse> findAllProfitniCentarResponse();
    void deleteBazniKontoById(Long bazniKontoId);
    Optional<BazniKonto> findBazniKontoById(Long bazniKontoId);
}