package raf.si.racunovodstvo.preduzece.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StazResponse implements Serializable {

    private Long stazId;
    private Date pocetakRada;
    private Date krajRada;
}
