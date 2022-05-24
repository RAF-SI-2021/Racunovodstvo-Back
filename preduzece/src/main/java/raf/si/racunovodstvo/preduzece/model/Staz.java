package raf.si.racunovodstvo.preduzece.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Staz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stazId;
    @Column
    private Date pocetakRada;
    @Column
    private Date krajRada;
}
