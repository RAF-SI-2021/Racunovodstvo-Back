package rs.raf.demo.services;

import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.model.Knjizenje;
import rs.raf.demo.model.enums.TipDokumenta;

import java.util.List;

public interface IKnjizenjeService extends IService<Knjizenje, Long>{

    List<Knjizenje> findAll(Specification<Knjizenje> spec);
    List<Knjizenje> findByTipDokumenta(TipDokumenta tipDokumenta);

}
