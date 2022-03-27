package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
public class KontnaGrupa {
    
    @Id
    @NotBlank(message = "Broj konta je obavezan")
    private String brojKonta;
    @Column(nullable = false)
    @NotBlank(message = "Naziv konta je obavezan")
    private String nazivKonta;
    @OneToMany(mappedBy = "kontnaGrupa", fetch =  FetchType.EAGER)
    private List<Konto> konto;


}
