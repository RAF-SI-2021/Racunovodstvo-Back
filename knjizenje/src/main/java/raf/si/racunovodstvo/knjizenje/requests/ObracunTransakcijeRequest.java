package raf.si.racunovodstvo.knjizenje.requests;

import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.validation.validator.ValidTransakcija;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ObracunTransakcijeRequest {

    @NotNull
    private String ime;
    @NotNull
    private String prezime;
    @NotNull
    private String sifraZaposlenog;
    @NotNull
    private Double iznos;
    @NotNull
    private Date datum;
    @NotNull
    private SifraTransakcije sifraTransakcije;
    @NotNull
    private Long preduzeceId;

}
