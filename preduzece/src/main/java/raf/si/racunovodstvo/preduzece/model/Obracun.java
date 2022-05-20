package raf.si.racunovodstvo.preduzece.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Date;
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
public class Obracun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long obracunId;
    @Column(nullable = false)
    private String naziv;
    @Column
    private String sifraTransakcije;
    @Column(nullable = false)
    private Date datumObracuna;
    @JsonIgnore
    @OneToMany(mappedBy = "obracun")
    @Cascade(CascadeType.ALL)
    private List<ObracunZaposleni> obracunZaposleniList;
}
