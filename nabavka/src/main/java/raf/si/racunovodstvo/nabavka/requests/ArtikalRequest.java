package raf.si.racunovodstvo.nabavka.requests;

import lombok.Data;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.validation.groups.OnCreate;
import raf.si.racunovodstvo.nabavka.validation.groups.OnUpdate;
import raf.si.racunovodstvo.nabavka.validation.validator.ValidArtikal;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@ValidArtikal
public class ArtikalRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long artikalId;
    @NotBlank
    private String sifraArtikla;
    @NotBlank
    private String nazivArtikla;
    @NotBlank
    private String jedinicaMere;
    @NotNull
    private Integer kolicina;
    @NotNull
    private Double nabavnaCena;
    @NotNull
    private Double rabatProcenat;
    @NotNull
    private Double marzaProcenat;
    @NotNull
    private Double prodajnaCena;
    private boolean aktivanZaProdaju;
    private Double porezProcenat;
    private Long konverzijaKalkulacijaId;

    @AssertTrue(message = "Polja ne mogu biti prazna")
    public boolean isValid() {
        return !aktivanZaProdaju || porezProcenat != null;
    }
}
