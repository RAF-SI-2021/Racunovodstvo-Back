package raf.si.racunovodstvo.nabavka.requests;

import lombok.*;
import raf.si.racunovodstvo.nabavka.model.enums.TipKalkulacije;
import raf.si.racunovodstvo.nabavka.validation.groups.OnCreate;
import raf.si.racunovodstvo.nabavka.validation.groups.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KalkulacijaRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long id;
    @NotNull(message = "Datum je obavezan")
    private Date datum;
    @NotNull(message = "Valuta je obavezna")
    private String valuta;
    @NotNull(message = "Broj kalkulacije je obavezan")
    private String brojKalkulacije;
    @NotNull(message = "Tip kalkulacije je obavezan")
    private TipKalkulacije tipKalkulacije;
    private Long dobavljacId;
    @Valid
    @NotNull
    private LokacijaRequest lokacija;
    private String komentar;
    @Valid
    private List<TroskoviNabavkeRequest> troskoviNabavke;
}
