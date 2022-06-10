package raf.si.racunovodstvo.nabavka.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.racunovodstvo.nabavka.model.enums.TipKalkulacije;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KalkulacijaResponse {

    private Long id;
    private Date datum;
    private String valuta;
    private String brojKalkulacije;
    private TipKalkulacije tipKalkulacije;
    private Long dobavljacId;
    private LokacijaResponse lokacija;
    private String komentar;
    private List<TroskoviNabavkeResponse> troskoviNabavke;
}
