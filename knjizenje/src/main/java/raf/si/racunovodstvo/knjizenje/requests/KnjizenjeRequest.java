package raf.si.racunovodstvo.knjizenje.requests;

import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.Dokument;
import raf.si.racunovodstvo.knjizenje.model.Konto;

import java.util.Date;
import java.util.List;

@Data
public class KnjizenjeRequest {


    private String brojNaloga;
    private Date datumKnjizenja;
    private Dokument dokument;
    private List<Konto> konto;
    private String komentar;
    private Long bazniCentarId;
}
