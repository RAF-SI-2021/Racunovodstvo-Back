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
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Kalkulacija extends BaznaKonverzijaKalkulacija {

    @Column(nullable = false, unique = true)
    @NotNull(message = "Broj kalkulacije je obavezan")
    private String brojKalkulacije;
    @Column(nullable = false)
    @NotNull(message = "Tip kalkulacije je obavezan")
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

        for (KalkulacijaArtikal artikal : this.artikli) {
            fakturnaCena += artikal.getUkupnaNabavnaCena();
            prodajnaCena += artikal.getUkupnaProdajnaCena();
        }
        this.setFakturnaCena(fakturnaCena);
        this.setProdajnaCena(prodajnaCena);
        this.setNabavnaCena(fakturnaCena + this.getTroskoviNabavke().stream().map(TroskoviNabavke::getCena).reduce(Double::sum).orElse(0.0));
    }
}
