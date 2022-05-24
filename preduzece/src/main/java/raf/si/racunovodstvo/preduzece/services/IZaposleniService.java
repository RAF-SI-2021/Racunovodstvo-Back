package raf.si.racunovodstvo.preduzece.services;

import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;

import java.util.List;

public interface IZaposleniService extends IService<Zaposleni, Long> {

    List<Zaposleni> findAll(Specification<Zaposleni> spec);
    Zaposleni otkazZaposleni(Zaposleni zaposleni);
    Zaposleni updateZaposleni(Zaposleni zaposleni);

}