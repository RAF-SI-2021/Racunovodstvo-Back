package raf.si.racunovodstvo.nabavka.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import raf.si.racunovodstvo.nabavka.model.enums.TipKalkulacije;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name = "kalkulacija")
@Getter
@Setter
public class Kalkulacija extends BaznaKonverzijaKalkulacija {

    @Column(nullable = false, unique = true)
    private String brojKalkulacije;
    @Column(nullable = false)
    private TipKalkulacije tipKalkulacije;
    @Column(nullable = false)
    private Double prodajnaCena;
    @OneToMany
    @JoinColumn(name = "kalkulacija")
    @Cascade(CascadeType.ALL)
    private List<KalkulacijaArtikal> artikli;

    public void calculateCene() {
        Double fakturnaCena = 0.0;
        Double prodajnaCena = 0.0;

        if (artikli != null)
            for (KalkulacijaArtikal artikal : this.artikli) {
                fakturnaCena += artikal.getUkupnaNabavnaVrednost();
                prodajnaCena += artikal.getUkupnaProdajnaVrednost();
            }

        this.setFakturnaCena(fakturnaCena);
        this.setProdajnaCena(prodajnaCena);
        if (this.getTroskoviNabavke() != null)
            this.setNabavnaVrednost(fakturnaCena + this.getTroskoviNabavke().stream().map(TroskoviNabavke::getCena).reduce(Double::sum).orElse(0.0));
        else
            this.setNabavnaVrednost(fakturnaCena);
    }
}
