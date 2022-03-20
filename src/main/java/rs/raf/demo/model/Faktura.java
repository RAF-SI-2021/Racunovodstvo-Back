package rs.raf.demo.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Faktura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fakturaId;
    @Column
    private String brojFakture;
    @Column
    private Date datumIzdavanja;
    @Column
    private Date datumPlacanja;
    @Column
    private Double prodajnaVrednost;
    @Column
    private String rabatProcenat;
    @Column
    private Double rabat;
    @Column
    private String porezProcenat;
    @Column
    private Double porez;
    @Column
    private Double iznos;
    @Column
    private String valuta;
    @Column
    private Double kurs;
    @Column
    private Double naplata;
    @Column
    private String komentar;
    @Column
    @Enumerated(EnumType.STRING)
    private TipFakture tipFakture;
    @ManyToOne
    @JoinColumn(name = "preduzeceId")
    private Preduzece preduzece;

    public Faktura(){}

}
