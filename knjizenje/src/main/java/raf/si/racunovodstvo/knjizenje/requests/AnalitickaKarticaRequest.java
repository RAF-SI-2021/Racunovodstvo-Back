package raf.si.racunovodstvo.knjizenje.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnalitickaKarticaRequest {

    @NotNull
    private Long kontnaGrupaId;
    @NotNull
    private String brojKonta;
    @NotNull
    private String nazivKonta;
}
