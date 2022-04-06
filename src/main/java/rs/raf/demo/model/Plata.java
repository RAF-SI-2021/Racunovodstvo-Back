package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Plata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plataId;
    @Column
    private Double porez;
    @Column
    private Double doprinos1;
    @Column
    private Double doprinos2;
    @Column
    private Double netoPlata;
    @Column
    private Double brutoPlata;
    @Column
    private Double ukupanTrosakZarade;
    @Column(nullable = false)
    private Date datum;
    @Column
    private String komentar;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "zaposleniId")
    private Zaposleni zaposleni;

}
