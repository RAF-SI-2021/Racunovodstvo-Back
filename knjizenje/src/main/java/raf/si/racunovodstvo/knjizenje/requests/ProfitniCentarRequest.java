package raf.si.racunovodstvo.knjizenje.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;

import javax.persistence.*;
import java.util.List;

@Data
public class ProfitniCentarRequest {

    private Long id;
    private String sifra;
    private String naziv;
    private Double ukupniTrosak;
    private Long lokacijaId;
    private Long odgovornoLiceId;
    private List<BazniKonto> kontoList;
    private ProfitniCentar parentProfitniCentar;
    private List<ProfitniCentar> profitniCentarList;
}
