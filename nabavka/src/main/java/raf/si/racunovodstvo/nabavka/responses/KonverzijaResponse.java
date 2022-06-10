package raf.si.racunovodstvo.nabavka.responses;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class KonverzijaResponse {

    private Long konverzijaId;
    private String brojKonverzije;
    private Date datum;
    private Long dobavljacId;
    private LokacijaResponse lokacija;
    private List<TroskoviNabavkeResponse> troskoviNabavke;
    private Double troskoviNabavkeSum;
    private Double fakturnaCena;
    private Double nabavnaVrednost;
    private String valuta;
    private String komentar;
}
