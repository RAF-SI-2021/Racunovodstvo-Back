package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
public class SifraTransakcije {

    @Id
    @NotBlank(message = "Sifra transakcije je obavezna!")
    private Long sifra;
    @Column(nullable = false)
    @NotBlank(message = "Naziv transakcije je obavezan!")
    private String nazivTransakcije;
    @JsonIgnore
    @OneToMany(mappedBy = "sifraTransakcije", fetch =  FetchType.EAGER)
    private List<Transakcija> transakcija;
}
