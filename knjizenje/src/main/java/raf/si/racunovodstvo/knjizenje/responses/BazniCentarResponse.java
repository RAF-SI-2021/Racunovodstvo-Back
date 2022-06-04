package raf.si.racunovodstvo.knjizenje.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.BazniCentar;
import raf.si.racunovodstvo.knjizenje.model.Konto;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
public class BazniCentarResponse {

    private Long id;
    private String sifra;
    private String naziv;
    private Double ukupniTrosak;
    private Long lokacijaId;
    private Long odgovornoLiceId;
    private List<Konto>  kontoList;
    private BazniCentar parentBazniCentar;
}
