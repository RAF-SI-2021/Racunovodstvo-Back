package rs.raf.demo.responses;

import lombok.Data;

import java.util.Date;

@Data
public class KnjizenjeResponse {
    Long knjizenjeId;
    String brojNaloga;
    Date datumKnjizenja;
    Long dokumentId;
    Double saldo;
    Double duguje;
    Double potrazuje;
    String komentar;
}
