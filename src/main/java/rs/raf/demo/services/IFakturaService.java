package rs.raf.demo.services;

import rs.raf.demo.model.Faktura;

import java.util.List;
import java.util.Map;

public interface IFakturaService extends IService<Faktura, Long>{

    public List<Faktura> findUlazneFakture();

    public List<Faktura> findIzlazneFakture();

    Map<String, Double> getSume(String tipFakture);
}
