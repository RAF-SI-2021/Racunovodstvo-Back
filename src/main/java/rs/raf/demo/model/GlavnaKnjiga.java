package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class GlavnaKnjiga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long glavnaKnjigaId;
    @ManyToOne
    @JoinColumn(name = "kontoId")
    private Konto konto;
    @ManyToOne
    @JoinColumn(name = "dnevnikKnjizenjaId")
    private DnevnikKnjizenja dnevnikKnjizenja;
    @Column
    private Double potrazuje;
    @Column(nullable = false)
    private Double saldo;


}
