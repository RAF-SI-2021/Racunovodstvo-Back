package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Knjizenje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long knjizenjeId;
    @Column(nullable = false)
    @NotNull(message = "Broj naloga je obavezan")
    private String brojNaloga;
    @Column(nullable = false)
    @NotNull(message = "Datum je obavezna")
    private Date datumKnjizenja;
    @ManyToOne
    @JoinColumn(name = "dokument")
    private Dokument dokument;
    @OneToMany(mappedBy = "knjizenje", fetch =  FetchType.EAGER)
    private List<Konto> konto;
    @Column
    private String komentar;
}
