package raf.si.racunovodstvo.knjizenje.responses;

import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;

import java.util.Date;

@Data
public class TransakcijaResponse {

    private Long transakcijaId;
    private String komitent;
    private String brojDokumenta;
    private String brojTransakcije;
    private Date datumTransakcije;
    private TipTransakcije tipTransakcije;
    private Double iznos;
    private String sadrzaj;
    private String komentar;
    private SifraTransakcijeResponse sifraTransakcije;
}
