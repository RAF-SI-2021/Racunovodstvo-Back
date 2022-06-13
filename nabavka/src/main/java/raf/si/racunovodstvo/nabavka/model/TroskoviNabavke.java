package raf.si.racunovodstvo.nabavka.model;

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

@Entity(name = "troskovi_nabavke")
@Getter
@Setter
public class TroskoviNabavke implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long troskoviNabavkeId;
    @Column(nullable = false)
    @NotNull(message = "Naziv je obavezan")
    private String naziv;
    @Column(nullable = false)
    @NotNull(message = "Cena je obavezna")
    private Double cena;
    @ManyToOne
    @JoinColumn(name="baznaKonverzijaKalkulacija")
    private BaznaKonverzijaKalkulacija baznaKonverzijaKalkulacija;
}
