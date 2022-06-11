package raf.si.racunovodstvo.nabavka.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class KalkulacijaArtikal extends Artikal {

    @Column
    private Double marzaProcenat;
    @Column
    private Double marza;
    @Column
    private Double prodajnaOsnovica;
    @Column
    private Double porezProcenat;
    @Column
    private Double porez;
    @Column
    private Double prodajnaCena;
    @Column
    private Double osnovica;
    @Column
    private Double ukupnaProdajnaVrednost;
    @ElementCollection
    private List<IstorijaProdajneCene> istorijaProdajneCene = new ArrayList<>();
}
