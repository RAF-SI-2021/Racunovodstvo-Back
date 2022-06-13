package raf.si.racunovodstvo.preduzece.services;

import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.responses.ZaposleniResponse;

import java.util.List;
import java.util.Optional;

public interface IZaposleniService extends IService<Zaposleni, Long> {

    List<ZaposleniResponse> findAll(Specification<Zaposleni> spec);
    ZaposleniResponse otkazZaposleni(Zaposleni zaposleni);
    ZaposleniResponse updateZaposleni(Zaposleni zaposleni);
    ZaposleniResponse saveZaposleni(Zaposleni zaposleni);
    Optional<ZaposleniResponse> findZaposleniById(Long id);
}
