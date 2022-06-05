package raf.si.racunovodstvo.preduzece.services;

import raf.si.racunovodstvo.preduzece.model.Obracun;

public interface IObracunService extends IService<Obracun, Long>{
    void updateObracunZaradeNaziv(Long obracunZaradeId, String naziv);
}
