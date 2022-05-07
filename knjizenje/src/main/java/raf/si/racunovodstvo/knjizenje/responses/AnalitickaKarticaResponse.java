package raf.si.racunovodstvo.knjizenje.responses;

import java.util.Date;

public class AnalitickaKarticaResponse {

    private String brojNaloga;
    private Date datum;
    private String brojDokumenta;
    private Double duguje;
    private Double potrazuje;
    private Double saldo;

    public AnalitickaKarticaResponse(String brojNaloga, Date datum, String brojDokumenta, Double duguje, Double potrazuje, Double saldo) {
        this.brojNaloga = brojNaloga;
        this.datum = datum;
        this.brojDokumenta = brojDokumenta;
        this.duguje = duguje;
        this.potrazuje = potrazuje;
        this.saldo = saldo;
    }
}
