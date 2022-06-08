package raf.si.racunovodstvo.preduzece.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "obracun")
@Getter
@Setter
public class Obracun implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long obracunId;
    @Column(nullable = false)
    private String naziv;
    @Column
    private String sifraTransakcije;
    @Column(nullable = false)
    private Date datumObracuna;
    @OneToMany(mappedBy = "obracun")
    @Cascade(CascadeType.ALL)
    private List<ObracunZaposleni> obracunZaposleniList;
}
