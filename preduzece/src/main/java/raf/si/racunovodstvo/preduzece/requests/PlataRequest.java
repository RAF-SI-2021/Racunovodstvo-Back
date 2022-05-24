package raf.si.racunovodstvo.preduzece.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Getter
@Setter
public class PlataRequest {
    private Long plataId;
    @NotNull(message = "netoPlata je obavezna")
    private Double netoPlata;
    @NotNull(message = "datum je obavezan")
    private Date datum;
    @NotNull(message = "zaposleniId je obavezan")
    private Long zaposleniId;
}
