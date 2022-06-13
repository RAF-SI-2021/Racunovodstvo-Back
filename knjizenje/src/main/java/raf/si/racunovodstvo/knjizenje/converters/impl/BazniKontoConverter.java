package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.Konto;

@Component
public class BazniKontoConverter {

    public BazniKonto convert(Konto konto){
        BazniKonto bazniKonto = new BazniKonto();
        bazniKonto.setDuguje(konto.getDuguje());
        bazniKonto.setPotrazuje(konto.getPotrazuje());
        bazniKonto.setKontnaGrupa(konto.getKontnaGrupa());
        bazniKonto.setBrojNalogaKnjizenja(konto.getKnjizenje().getBrojNaloga());
        bazniKonto.setDatumKnjizenja(konto.getKnjizenje().getDatumKnjizenja());
        bazniKonto.setKomentarKnjizenja(konto.getKnjizenje().getKomentar());
        return bazniKonto;
    }
}
