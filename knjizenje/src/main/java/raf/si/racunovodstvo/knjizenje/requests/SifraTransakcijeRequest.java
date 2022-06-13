package raf.si.racunovodstvo.knjizenje.requests;

import lombok.Data;
import raf.si.racunovodstvo.knjizenje.validation.groups.OnCreate;
import raf.si.racunovodstvo.knjizenje.validation.groups.OnUpdate;
import raf.si.racunovodstvo.knjizenje.validation.validator.ValidTransakcija;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@ValidTransakcija
public class SifraTransakcijeRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long sifraTransakcijeId;
    @NotNull
    private Long sifra;
    @NotNull
    private String nazivTransakcije;
}
