package raf.si.racunovodstvo.preduzece.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.si.racunovodstvo.preduzece.model.enums.PolZaposlenog;
import raf.si.racunovodstvo.preduzece.model.enums.RadnaPozicija;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZaposleniResponse implements Serializable {

    private Long zaposleniId;
    private String ime;
    private String prezime;
    private String imeRoditelja;
    private Date pocetakRadnogOdnosa;
    private String jmbg;
    private PolZaposlenog pol;
    private Date datumRodjenja;
    private String adresa;
    private String grad;
    private String brojRacuna;
    private String stepenObrazovanja;
    private Long brojRadneKnjizice;
    private List<StazResponse> staz;
    private StatusZaposlenog statusZaposlenog;
    private String komentar;
    private PreduzeceResponse preduzece;
    private RadnaPozicija radnaPozicija;
}
