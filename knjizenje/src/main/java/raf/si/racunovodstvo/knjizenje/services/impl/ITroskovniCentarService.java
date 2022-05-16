package raf.si.racunovodstvo.knjizenje.services.impl;

import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;

public interface ITroskovniCentarService extends IService<TroskovniCentar, Long> {

    TroskovniCentar addKontosIntoTroskovniCentar(Knjizenje knjizejne, TroskovniCentar troskovniCentar);
    void deleteKontoFromTroskovniCentar(Konto konto , TroskovniCentar troskovniCentar);
    void updateKontoInTroskovniCentar(Konto konto, TroskovniCentar troskovniCentar);
}
