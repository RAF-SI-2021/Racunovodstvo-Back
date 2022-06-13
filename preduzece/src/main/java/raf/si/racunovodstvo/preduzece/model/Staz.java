package raf.si.racunovodstvo.preduzece.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "staz")
@Getter
@Setter
public class Staz implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stazId;
    @Column
    private Date pocetakRada;
    @Column
    private Date krajRada;
    @ManyToOne
    @JoinColumn(name = "zaposleniId")
    @JsonIgnore
    private Zaposleni zaposleni;
}
