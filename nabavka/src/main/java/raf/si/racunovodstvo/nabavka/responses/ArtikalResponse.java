package raf.si.racunovodstvo.nabavka.responses;

import lombok.Data;
import raf.si.racunovodstvo.nabavka.model.IstorijaProdajneCene;

import java.io.Serializable;
import java.util.List;

@Data
public class ArtikalResponse implements Serializable {

    private Long artikalId;
    private String sifraArtikla;
    private String nazivArtikla;
    private String jedinicaMere;
    private Integer kolicina;
    private Double nabavnaCena;
    private Double rabatProcenat;
    private Double rabat;
    private Double nabavnaCenaPosleRabata;
    private Double ukupnaNabavnaVrednost;
    private Double marzaProcenat;
    private Double marza;
    private Double prodajnaOsnovica;
    private Double porezProcenat;
    private Double porez;
    private Double prodajnaCena;
    private Double osnovica;
    private Double ukupnaProdajnaVrednost;
    private List<IstorijaProdajneCene> istorijaProdajneCene;
    private Long konverzijaKalkulacijaId;
}
