package raf.si.racunovodstvo.knjizenje.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity(name = "knjizenje")
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
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "knjizenje")
    private List<Konto> konto;
    @Column
    private String komentar;
}
