package raf.si.racunovodstvo.knjizenje.services.impl;

import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;

public interface IProfitniCentarService extends IService<ProfitniCentar,Long> {

    ProfitniCentar addKontosIntoProfitniCentar(Knjizenje knjizejne, ProfitniCentar profitniCentar);
    void deleteKontoFromProfitniCentar(Konto konto, ProfitniCentar profitniCentar);
    void updateKontoInProfitniCentar(Konto konto, ProfitniCentar profitniCentar);

}
