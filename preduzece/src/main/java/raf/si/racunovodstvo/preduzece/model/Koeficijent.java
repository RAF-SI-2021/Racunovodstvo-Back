package raf.si.racunovodstvo.preduzece.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "koeficijent")
@Getter
@Setter
public class Koeficijent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long koeficijentId;
    @Column
    private Double penzionoOsiguranje1;
    @Column
    private Double penzionoOsiguranje2;
    @Column
    private Double zdravstvenoOsiguranje1;
    @Column
    private Double zdravstvenoOsiguranje2;
    @Column
    private Double nezaposlenost1;
    @Column
    private Double nezaposlenost2;
    @Column
    private Double najnizaOsnovica;
    @Column
    private Double najvisaOsnovica;
    @Column
    private Double poreskoOslobadjanje;
    @Column
    private Double koeficijentPoreza;
    @Column
    private boolean status;
    @Column
    private Date date;

}
