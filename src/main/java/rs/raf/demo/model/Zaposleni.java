package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import rs.raf.demo.model.enums.PolZaposlenog;
import rs.raf.demo.model.enums.RadnaPozicija;
import rs.raf.demo.model.enums.StatusZaposlenog;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Zaposleni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zaposleniId;
    @Column(nullable = false)
    @NotBlank(message = "Ime je obavezno")
    private String ime;
    @Column(nullable = false)
    @NotBlank(message = "Prezime je obavezno")
    private String prezime;
    @Column
    private String imeRoditelja;
    @Column(nullable = false)
    private Date pocetakRadnogOdnosa;
    @Column(nullable = false)
    @NotBlank(message = "JMBG je obavezan")
    @Size(min = 13,max = 13)
    @Pattern(regexp="^(0|[1-9][0-9]*)$")
    private String jmbg;
    @Column(nullable = false)
    @NotNull(message = "Pol je obavezan")
    @Enumerated(EnumType.STRING)
    private PolZaposlenog pol;
    @Column(nullable = false)
    private Date datumRodjenja;
    @Column
    private String adresa;
    @Column
    private String grad;
    @Column
    private String brojRacuna;
    @Column
    private String stepenObrazovanja;
    @Column
    private Long brojRadneKnjizice;
    @OneToMany
    private List<Staz> staz;
    @Column
    @Enumerated(EnumType.STRING)
    private StatusZaposlenog statusZaposlenog;
    @Column
    private String komentar;
    @ManyToOne
    @JoinColumn(name = "preduzeceId")
    private Preduzece preduzece;
    @Column
    @Enumerated(EnumType.STRING)
    private RadnaPozicija radnaPozicija;

}
