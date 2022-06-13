package raf.si.racunovodstvo.knjizenje.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "konto")
@Getter
@Setter
public class Konto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kontoId;
    @Column
    private Double potrazuje;
    @Column
    private Double duguje;
    @ManyToOne
    @JoinColumn(name = "kontnaGrupaId", nullable = false, updatable = false)
    private KontnaGrupa kontnaGrupa;
    @ManyToOne
    @JoinColumn(name = "bazniCentarId")
    private BazniCentar bazniCentar;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "knjizenjeId", nullable = false)
    private Knjizenje knjizenje;
}
