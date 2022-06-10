package raf.si.racunovodstvo.nabavka.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PreduzeceResponse {

    private Long preduzeceId;
    private String naziv;
    private String pib;
    private String racun;
    private String grad;
    private String adresa;
    private String telefon;
    private String email;
    private String fax;
    private String webAdresa;
    private String komentar;
    private Boolean isActive;
}
