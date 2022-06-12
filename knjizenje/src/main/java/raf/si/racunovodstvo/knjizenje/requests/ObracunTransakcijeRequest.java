package raf.si.racunovodstvo.knjizenje.requests;

import lombok.Data;

import java.util.Date;

import javax.validation.constraints.NotNull;

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