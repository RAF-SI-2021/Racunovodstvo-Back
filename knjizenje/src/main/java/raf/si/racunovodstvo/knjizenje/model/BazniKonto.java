package raf.si.racunovodstvo.knjizenje.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
public class BazniKonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bazniKontoId;
    @Column
    private Double potrazuje;
    @Column
    private Double duguje;
    @ManyToOne
    @JoinColumn(name = "kontnaGrupaId", nullable = false)
    private KontnaGrupa kontnaGrupa;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "bazniCentarId", nullable = false)
    private BazniCentar bazniCentar;
    @Column(nullable = false)
    private String brojNalogaKnjizenja;
    @Column(nullable = false)
    private Date datumKnjizenja;
    @Column
    private String komentarKnjizenja;

}
