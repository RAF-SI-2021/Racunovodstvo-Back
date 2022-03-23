package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
public class Konto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kontoId;
    @Column(nullable = false)
    @NotBlank(message = "Broj konta je obavezan")
    private String brojKonta;
    @Column(nullable = false)
    @NotBlank(message = "Naziv konta je obavezan")
    private String naziv;
    @ManyToMany(mappedBy = "konto", fetch =  FetchType.EAGER)
    private List<GlavnaKnjiga> glavnaKnjiga;
}
