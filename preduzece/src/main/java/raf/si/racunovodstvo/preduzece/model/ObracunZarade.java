package raf.si.racunovodstvo.preduzece.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
public class ObracunZarade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long obracunZaradeId;
    @Column
    private String naziv;
    @Column
    private Date datumObracuna;
    @Column
    private Double porez;
    @Column
    private Double doprinos1;
    @Column
    private Double doprinos2;
    @Column
    @NotNull(message = "netoPlata je obavezna")
    private Double netoPlata;
    @Column
    private Double brutoPlata;
    @Column
    private Double ukupanTrosakZarade;
    @Column(nullable = false)
    private Date datumOd;
    @Column
    private Date datumDo;
    @Column
    private String komentar;
    @Column
    private Double ucinak;
    @ManyToOne
    @JoinColumn(name = "zaposleniId")
    private Zaposleni zaposleni;
}
