package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "dnevnikKnjizenja")
public class DnevnikKnjizenja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dnevnikKnjizenjaId;
    @Column(nullable = false)
    @NotBlank(message = "Broj naloga je obavezna")
    private int brojNaloga;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brojDokumenta", referencedColumnName ="fakturaId")
    private Faktura faktura;
    @Column(nullable = false)
    @NotBlank(message = "Datum je obavezna")
    private Date datumKnjizenja;
    @Column
    private Double duguje;
    @Column
    private Double potrazuje;
    @Column(nullable = false)
    private Double saldo;
    @Column
    private String komentar;
    @ManyToMany(mappedBy = "dnevnikKnjizenja", fetch =  FetchType.EAGER)
    private List<GlavnaKnjiga> glavnaKnjiga;
}
