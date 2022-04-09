package raf.si.racunovodstvo.knjizenje.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class FakturaWithPreduzece{

    private String brojFakture;
    private Date datumIzdavanja;
    private Date rokZaPlacanje;
    private Date datumPlacanja;
    private Double prodajnaVrednost;
    private Double rabatProcenat;
    private Double rabat;
    private Double porezProcenat;
    private Double porez;
    private Double iznos;
    private String valuta;
    private Double kurs;
    private Double naplata;
    private String komentar;
    private TipFakture tipFakture;
    private Preduzece preduzece;

    public FakturaWithPreduzece(Faktura f, Preduzece p){
        this(f.getBrojFakture(),
                f.getDatumIzdavanja(),
                f.getRokZaPlacanje(),
                f.getDatumPlacanja(),
                f.getProdajnaVrednost(),
                f.getRabatProcenat(),
                f.getRabat(),
                f.getPorezProcenat(),
                f.getPorez(),
                f.getIznos(),
                f.getValuta(),
                f.getKurs(),
                f.getNaplata(),
                f.getKomentar(),
                f.getTipFakture(),p);
    }

}
