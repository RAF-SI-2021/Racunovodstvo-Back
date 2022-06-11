package raf.si.racunovodstvo.nabavka.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.requests.KonverzijaRequest;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;

public interface IKonverzijaService extends IService<Konverzija, Long> {

    Page<KonverzijaResponse> findAll(Specification<Konverzija> spec, Pageable pageSort );

    KonverzijaResponse saveKonverzija(KonverzijaRequest konverzijaRequest);

    Konverzija increaseNabavnaCena(Long konverzijaId, Double increaseBy);
}
