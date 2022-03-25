package rs.raf.demo.services;

import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.model.Faktura;

import java.util.List;
import java.util.Map;

public interface IFakturaService extends IService<Faktura, Long>{

    List<Faktura> findAll(Specification<Faktura> spec);

    List<Faktura> findUlazneFakture();

    List<Faktura> findIzlazneFakture();

    Map<String, Double> getSume(String tipFakture);
}
