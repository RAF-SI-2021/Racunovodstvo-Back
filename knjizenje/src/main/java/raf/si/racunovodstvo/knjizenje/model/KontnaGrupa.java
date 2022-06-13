package raf.si.racunovodstvo.knjizenje.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity(name = "kontna_grupa")
@Getter
@Setter
public class KontnaGrupa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kontnaGrupaId;
    @Column(nullable = false)
    @NotBlank(message = "Broj konta je obavezan")
    private String brojKonta;
    @Column(nullable = false)
    @NotBlank(message = "Naziv konta je obavezan")
    private String nazivKonta;
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "kontnaGrupa")
    private List<Konto> konto;
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "kontnaGrupa")
    private List<BazniKonto> bazniKonto;


}
