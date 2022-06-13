package raf.si.racunovodstvo.knjizenje.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
public class SifraTransakcije {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sifraTransakcijeId;
    @Column(nullable = false, unique = true)
    @NotNull(message = "Sifra transakcije je obavezna i jedinstvena!")
    private Long sifra;
    @Column(nullable = false)
    @NotBlank(message = "Naziv transakcije je obavezan!")
    private String nazivTransakcije;
    @JsonIgnore
    @OneToMany(mappedBy = "sifraTransakcije", fetch =  FetchType.EAGER)
    private List<Transakcija> transakcija;
}