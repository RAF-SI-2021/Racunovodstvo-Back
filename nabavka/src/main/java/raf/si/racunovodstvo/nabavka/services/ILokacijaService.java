package raf.si.racunovodstvo.nabavka.services;

import raf.si.racunovodstvo.nabavka.model.Lokacija;

public interface ILokacijaService extends IService<Lokacija, Long>{

    Lokacija update(Lokacija lokacija);

}
