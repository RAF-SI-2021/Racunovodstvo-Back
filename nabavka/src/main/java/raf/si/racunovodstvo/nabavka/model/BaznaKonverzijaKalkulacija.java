package raf.si.racunovodstvo.nabavka.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Getter
@Setter
public class BaznaKonverzijaKalkulacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Date datum;
    @Column(nullable = false)
    private Long dobavljacId;
    @ManyToOne(optional = false)
    private Lokacija lokacija;
    @OneToMany(mappedBy = "baznaKonverzijaKalkulacija", fetch = FetchType.EAGER)
    private List<TroskoviNabavke> troskoviNabavke;
    @Column(nullable = false)
    private Double fakturnaCena;
    @Column(nullable = false)
    private Double nabavnaCena;
    @Column(nullable = false)
    private String valuta;
    @Column
    private String komentar;
}
