package raf.si.racunovodstvo.preduzece.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name = "obracun_zaposleni")
@Getter
@Setter
public class ObracunZaposleni implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long obracunZaposleniId;
    @Column(nullable = false)
    private Double ucinak;
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
    @Column
    private String komentar;
    @ManyToOne
    @JoinColumn(name = "zaposleniId")
    private Zaposleni zaposleni;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "obracunId")
    private Obracun obracun;
}
