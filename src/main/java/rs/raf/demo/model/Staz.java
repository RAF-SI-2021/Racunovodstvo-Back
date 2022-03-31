package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Staz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stazId;
    @Column
    private Date pocetakRada;
    @Column
    private Date krajRada;
    @ManyToOne
    @JoinColumn(name = "zaposleniId")
    private Zaposleni zaposleni;
}
