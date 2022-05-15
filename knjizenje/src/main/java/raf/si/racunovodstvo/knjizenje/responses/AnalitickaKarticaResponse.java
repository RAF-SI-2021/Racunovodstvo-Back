package raf.si.racunovodstvo.knjizenje.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


@Data
@AllArgsConstructor
public class AnalitickaKarticaResponse {

    private String brojNaloga;
    private Date datumKnjizenja;
    private Long brojDokumenta;
    private Double duguje;
    private Double potrazuje;
    private Double saldo;
}
