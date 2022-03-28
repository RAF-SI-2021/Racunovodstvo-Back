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
public class Knjizenje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long knjizenjeId;
    @Column(nullable = false)
    @NotBlank(message = "Datum je obavezna")
    private Date datumKnjizenja;
    @ManyToOne
    @JoinColumn(name = "knjizenje")
    private Dokument dokument;
    @OneToMany(mappedBy = "knjizenje", fetch =  FetchType.EAGER)
    private List<Konto> konto;
}
