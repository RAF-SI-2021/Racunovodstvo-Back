package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.responses.FakturaResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IFakturaService extends IService<Faktura, Long> {

    List<Faktura> findAll(Specification<Faktura> spec);

    Page<Faktura> findAll(Pageable pageSort);

    Map<String, Double> getSume(String tipFakture);

    Optional<FakturaResponse> findFakturaById(Long id);

    FakturaResponse saveFaktura(Faktura faktura);
}
