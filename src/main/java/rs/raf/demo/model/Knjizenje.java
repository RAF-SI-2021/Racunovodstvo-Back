package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @NotNull(message = "Datum je obavezna")
    private Date datumKnjizenja;
    @ManyToOne
    @JoinColumn(name = "knjizenje")
    private Dokument dokument;
    @JsonIgnore
    @OneToMany(mappedBy = "knjizenje", fetch =  FetchType.EAGER)
    private List<Konto> konto;
}
