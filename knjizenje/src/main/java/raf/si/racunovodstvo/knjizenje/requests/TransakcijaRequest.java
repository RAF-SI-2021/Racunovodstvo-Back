package raf.si.racunovodstvo.knjizenje.requests;

import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;
import raf.si.racunovodstvo.knjizenje.validation.groups.OnCreate;
import raf.si.racunovodstvo.knjizenje.validation.groups.OnUpdate;
import raf.si.racunovodstvo.knjizenje.validation.validator.ValidTransakcija;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Data
@ValidTransakcija
public class TransakcijaRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long dokumentId;
    @NotNull
    private String brojDokumenta;
    @NotNull
    private TipDokumenta tipDokumenta;
    @NotNull
    private String brojTransakcije;
    @NotNull
    private Date datumTransakcije;
    @NotNull
    private TipTransakcije tipTransakcije;
    @NotNull
    private Double iznos;
    private String sadrzaj;
    private String komentar;
    @NotNull
    private SifraTransakcije sifraTransakcije;
    private Long preduzeceId;
}
