package raf.si.racunovodstvo.knjizenje.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FakturaResponse implements Serializable {

    private Long dokumentId;
    private String brojDokumenta;
    private TipDokumenta tipDokumenta;
    private List<KnjizenjeResponse> knjizenje;
    private Long preduzeceId;
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
}
