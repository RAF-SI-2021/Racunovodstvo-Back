package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;

public interface IKontnaGrupaService extends IService<KontnaGrupa, Long> {
    Page<KontnaGrupa> findAll(Pageable sort);
    KontnaGrupa findKontnaGrupaById(Long id);
}
