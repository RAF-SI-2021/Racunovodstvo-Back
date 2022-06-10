package raf.si.racunovodstvo.nabavka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Getter
@Setter
public class Lokacija {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lokacijaId;
    @Column(nullable = false)
    private String naziv;
    @Column(nullable = false)
    private String adresa;
    @JsonIgnore
    @OneToMany(mappedBy = "lokacija")
    private List<BaznaKonverzijaKalkulacija> baznaKonverzijaKalkulacijaList;
}
