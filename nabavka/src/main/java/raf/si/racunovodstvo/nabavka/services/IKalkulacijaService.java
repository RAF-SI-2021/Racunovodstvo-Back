package raf.si.racunovodstvo.nabavka.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.requests.KalkulacijaRequest;
import raf.si.racunovodstvo.nabavka.responses.KalkulacijaResponse;

import java.util.List;
import java.util.Map;

public interface IKalkulacijaService extends IService<Kalkulacija, Long> {

    Page<KalkulacijaResponse> findAll(Pageable pageable);

    Page<KalkulacijaResponse> findAll(Specification<Kalkulacija> spec, Pageable pageSort);

    Page<Kalkulacija> findAllKalkulacije(Specification<Kalkulacija> spec, Pageable pageable);

    Map<String, Number> getTotalKalkulacije(List<Kalkulacija> kalulacije);

    Kalkulacija increaseNabavnaAndProdajnaCena(Long kalkulacijaId, Double nabavnaCena, Double prodajnaCena);

    KalkulacijaResponse save(KalkulacijaRequest kalkulacija);

    KalkulacijaResponse update(KalkulacijaRequest kalkulacija);
}
