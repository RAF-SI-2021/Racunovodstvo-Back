package raf.si.racunovodstvo.knjizenje.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;

import java.util.List;

@Data
@AllArgsConstructor
public class TroskovniCentarResponse {

    private Long id;
    private String sifra;
    private String naziv;
    private Double ukupniTrosak;
    private Long lokacijaId;
    private Long odgovornoLiceId;
    private List<BazniKonto>  kontoList;
    private TroskovniCentar parentTroskovniCentar;
}
