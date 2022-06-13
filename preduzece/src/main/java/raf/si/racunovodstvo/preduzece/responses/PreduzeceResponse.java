package raf.si.racunovodstvo.preduzece.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreduzeceResponse implements Serializable {

    private Long preduzeceId;
    private String naziv;
    private String pib;
    private String racun;
    private String adresa;
    private String grad;
    private String telefon;
    private String email;
    private String fax;
    private String webAdresa;
    private String komentar;
    private Boolean isActive;
}
