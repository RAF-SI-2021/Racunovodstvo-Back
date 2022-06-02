package raf.si.racunovodstvo.knjizenje.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalitickaKarticaResponse {

    private String brojNaloga;
    private Date datumKnjizenja;
    private String brojDokumenta;
    private Double duguje;
    private Double potrazuje;
    private Double saldo;
}
