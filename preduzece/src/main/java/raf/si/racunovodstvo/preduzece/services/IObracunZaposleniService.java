package raf.si.racunovodstvo.preduzece.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaposleniRequest;


public interface IObracunZaposleniService extends IService<ObracunZaposleni, Long>{

    ObracunZaposleni save(ObracunZaposleniRequest obracunZaposleniRequest);

    ObracunZaposleni update(Double ucinak, Double netoPlata, Long idObracunZaposleni);

    Page<ObracunZaposleni> findAll(Specification<ObracunZaposleni> spec, Pageable pageSort);

    Page<ObracunZaposleni> findAll(Pageable pageSort);
}
