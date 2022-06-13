package raf.si.racunovodstvo.preduzece.services;

import raf.si.racunovodstvo.preduzece.model.Preduzece;
import raf.si.racunovodstvo.preduzece.responses.PreduzeceResponse;

import java.util.List;
import java.util.Optional;

public interface IPreduzeceService extends IService<Preduzece, Long> {

    PreduzeceResponse savePreduzece(Preduzece preduzece);
    Optional<PreduzeceResponse> findPreduzeceById(Long id);
    List<PreduzeceResponse> findAllPreduzece();
}
