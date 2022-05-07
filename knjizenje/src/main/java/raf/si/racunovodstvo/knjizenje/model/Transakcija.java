package raf.si.racunovodstvo.knjizenje.model;

import lombok.Getter;
import lombok.Setter;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "transakcijaId")
public class Transakcija extends Dokument{

    @Column(nullable = false,unique = true)
    private Long brojTransakcije;
    @Column(nullable = false)
    private Date datumTransakcije;
    @Column(nullable = false)
    private TipTransakcije tipTransakcije;
    @Column(nullable = false)
    private Double iznos;
    @Column
    private String sadrzaj;
    @Column
    private String komentar;
    @ManyToOne
    @JoinColumn(name = "sifra")
    private SifraTransakcije sifraTransakcije;
}
