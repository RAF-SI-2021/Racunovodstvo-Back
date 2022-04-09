package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "netoPlata je obavezna")
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
    @JoinColumn(name = "zaposleniId")
    private Zaposleni zaposleni;

    public void izracunajDoprinose(Koeficijent koeficijent) {
        double b;
        if (this.netoPlata < koeficijent.getNajnizaOsnovica()) {
            b = (this.netoPlata - 1.93 + (koeficijent.getNajnizaOsnovica() * 19.9)) / 0.9;
        }
        else if (this.netoPlata < koeficijent.getNajvisaOsnovica()) {
            b = (this.netoPlata - 1.93) / 0.701;
        }
        else {
            b = (this.netoPlata - 1.93 + (koeficijent.getNajvisaOsnovica() * 19.9)) / 0.9;
        }
        this.doprinos1 = b * (koeficijent.getPenzionoOsiguranje1() + koeficijent.getZdravstvenoOsiguranje1() + koeficijent.getNezaposlenost1());
        this.doprinos2 = b * (koeficijent.getPenzionoOsiguranje2() + koeficijent.getZdravstvenoOsiguranje2() + koeficijent.getNezaposlenost2());
        this.porez = (b - koeficijent.getPoreskoOslobadjanje()) * koeficijent.getKoeficijentPoreza();
        this.brutoPlata = this.netoPlata + this.porez;
        this.ukupanTrosakZarade = this.brutoPlata + this.doprinos2;
    }
}
