package raf.si.racunovodstvo.knjizenje.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity(name = "bazni_centar")
@Getter
@Setter
public abstract class BazniCentar {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable = false)
    private String sifra;
    @Column(nullable = false)
    private String naziv;
    @Column(nullable = false)
    private Double ukupniTrosak;
    @Column(nullable = false)
    private Long lokacijaId;
    @Column(nullable = false)
    private Long odgovornoLiceId;
    @JsonIgnore
    @OneToMany(mappedBy = "bazniCentar")
    private List<Konto> kontoList;
}
