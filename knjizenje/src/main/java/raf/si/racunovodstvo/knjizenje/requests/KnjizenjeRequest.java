package raf.si.racunovodstvo.knjizenje.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.si.racunovodstvo.knjizenje.model.Dokument;
import raf.si.racunovodstvo.knjizenje.model.Konto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnjizenjeRequest {

    @NotBlank
    private String brojNaloga;
    @NotNull
    private Date datumKnjizenja;
    @NotNull
    private Dokument dokument;
    private List<Konto> konto;
    private String komentar;
    private Long bazniCentarId;
}
