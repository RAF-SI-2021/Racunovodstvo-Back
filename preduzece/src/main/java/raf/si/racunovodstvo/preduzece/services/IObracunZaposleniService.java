package raf.si.racunovodstvo.preduzece.services;

import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;


public interface IObracunZaposleniService extends IService<ObracunZaposleni, Long>{

    ObracunZaposleni save(Long zaposleniId, Double ucinak, Long obracunId);
}
