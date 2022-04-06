package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
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
    @JoinColumn(name = "kontnaGrupaId")
    private KontnaGrupa kontnaGrupa;
    @ManyToOne
    @JoinColumn(name = "knjizenjeId")
    private Knjizenje knjizenje;
}
