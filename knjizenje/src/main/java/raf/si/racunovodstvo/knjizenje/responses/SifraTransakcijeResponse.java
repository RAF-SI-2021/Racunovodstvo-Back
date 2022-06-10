package raf.si.racunovodstvo.knjizenje.responses;

import lombok.Data;

@Data
public class SifraTransakcijeResponse {

    private Long sifraTransakcijeId;
    private Long sifra;
    private String nazivTransakcije;
}
