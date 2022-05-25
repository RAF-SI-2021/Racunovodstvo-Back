package raf.si.racunovodstvo.nabavka.requests;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.racunovodstvo.nabavka.validation.groups.OnCreate;
import raf.si.racunovodstvo.nabavka.validation.groups.OnUpdate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class KonverzijaRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long id;
    @NotNull(message = "brojKonverzije je obavezna")
    private String brojKonverzije;
    @NotNull(message = "datum je obavezna")
    private Date datum;
    @NotNull(message = "dobavljacId je obavezna")
    private Long dobavljacId;
    @NotNull(message = "lokacijaId je obavezna")
    private Long lokacijaId;
    @NotNull(message = "valuta je obavezna")
    private String valuta;
    private String komentar;

}
