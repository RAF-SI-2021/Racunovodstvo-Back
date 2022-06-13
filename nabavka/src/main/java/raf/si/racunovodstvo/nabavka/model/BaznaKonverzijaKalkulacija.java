package raf.si.racunovodstvo.nabavka.model;

import lombok.Getter;
import lombok.Setter;
import raf.si.racunovodstvo.nabavka.auditor.BaznaKonverzijaKalkulacijaAuditor;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@EntityListeners(BaznaKonverzijaKalkulacijaAuditor.class)
@Entity(name = "bazna_konverzija_kalkulacija")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class BaznaKonverzijaKalkulacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Date datum;
    @Column(nullable = false)
    private Long dobavljacId;
    @ManyToOne
    @JoinColumn(name = "lokacijaId")
    private Lokacija lokacija;
    @OneToMany(mappedBy = "baznaKonverzijaKalkulacija", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TroskoviNabavke> troskoviNabavke;
    @Column(nullable = false)
    private Double fakturnaCena;
    @Column(nullable = false)
    private Double nabavnaVrednost;
    @Column(nullable = false)
    private String valuta;
    @Column
    private String komentar;
}
