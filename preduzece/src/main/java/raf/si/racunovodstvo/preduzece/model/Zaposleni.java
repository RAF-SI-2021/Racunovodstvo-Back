package raf.si.racunovodstvo.preduzece.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import raf.si.racunovodstvo.preduzece.model.enums.PolZaposlenog;
import raf.si.racunovodstvo.preduzece.model.enums.RadnaPozicija;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity(name = "zaposleni")
@Getter
@Setter
public class Zaposleni implements Serializable {

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
    @Size(min = 13, max = 13)
    @Pattern(regexp = "^(0|[1-9][0-9]*)$")
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
    @OneToMany(mappedBy = "zaposleni")
    private List<Staz> staz;
    @JsonIgnore
    @OneToMany(mappedBy = "zaposleni")
    private List<Plata> plata;
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
