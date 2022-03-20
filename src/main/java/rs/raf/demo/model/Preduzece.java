package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Preduzece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preduzeceId;
    @Column(nullable = false)
    private String naziv;
    @Column(nullable = false)
    private int PIB;
    @Column(nullable = false)
    private String adresa;
    @Column(nullable = false)
    private String grad;
    @Column
    private String brojTelefona;
    @Column
    private Long brojRacuna;
    @Column
    private String eMail;
    @Column
    private String webAdresa;

    @JsonIgnore
    @OneToMany(mappedBy = "preduzece", fetch =  FetchType.EAGER)
    private List<Faktura> fakture;

    public Preduzece(){}

    public Preduzece(String naziv,int PIB, String adresa, String grad){
        this.naziv = naziv;
        this.PIB = PIB;
        this.adresa = adresa;
        this.grad = grad;
    }
}
