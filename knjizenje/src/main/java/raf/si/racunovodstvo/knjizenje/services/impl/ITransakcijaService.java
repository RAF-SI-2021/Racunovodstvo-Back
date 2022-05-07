package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;

import java.util.List;

public interface ITransakcijaService extends IService<Transakcija, Long> {

    List<Transakcija> findAll(Specification<Transakcija> spec);
    Page<Transakcija> findAll(Pageable pageSort);

}
