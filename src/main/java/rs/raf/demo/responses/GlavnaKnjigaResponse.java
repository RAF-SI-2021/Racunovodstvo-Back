package rs.raf.demo.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GlavnaKnjigaResponse {
    Long brojNaloga;
    Date datum;
    Double potrazuje;
    Double duguje;
    Double saldo;
    String nazivKonta;
    String konto;
}
