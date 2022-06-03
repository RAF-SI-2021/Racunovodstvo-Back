package raf.si.racunovodstvo.knjizenje.requests;

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
public class PovracajRequest {
    private Long povracajId;
    @NotNull(message = "brojPovracaja je obavezno polje")
    private String brojPovracaja;
    @NotNull(message = "prodajnaVrednost je obavezno polje")
    private Double prodajnaVrednost;
    private Date datumPovracaja;
}
