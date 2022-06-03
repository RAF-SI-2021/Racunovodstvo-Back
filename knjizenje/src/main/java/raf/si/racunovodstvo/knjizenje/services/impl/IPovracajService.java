package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.knjizenje.model.Povracaj;

public interface IPovracajService extends IService<Povracaj, Long> {
    Page<Povracaj> findAll(Pageable pageSort);
}
