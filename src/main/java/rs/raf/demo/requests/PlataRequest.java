package rs.raf.demo.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

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
