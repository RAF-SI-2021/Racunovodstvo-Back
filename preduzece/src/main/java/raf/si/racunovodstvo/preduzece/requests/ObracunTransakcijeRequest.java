package raf.si.racunovodstvo.preduzece.requests;

import lombok.Data;

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
    private Long sifraTransakcijeId;
    @NotNull
    private Long preduzeceId;

}