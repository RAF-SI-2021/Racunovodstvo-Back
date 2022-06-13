package raf.si.racunovodstvo.preduzece.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlataResponse implements Serializable {

    private Long plataId;
    private Double porez;
    private Double doprinos1;
    private Double doprinos2;
    private Double netoPlata;
    private Double brutoPlata;
    private Double ukupanTrosakZarade;
    private Date datumOd;
    private Date datumDo;
    private String komentar;
    private ZaposleniResponse zaposleni;
}
