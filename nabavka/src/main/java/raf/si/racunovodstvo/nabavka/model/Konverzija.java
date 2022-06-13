package raf.si.racunovodstvo.nabavka.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name = "konverzija")
@Getter
@Setter
public class Konverzija extends BaznaKonverzijaKalkulacija {

    @Column(nullable = false, unique = true)
    private String brojKonverzije;
    @OneToMany
    @JoinColumn(name = "konverzija")
    private List<KonverzijaArtikal> artikli;
}
