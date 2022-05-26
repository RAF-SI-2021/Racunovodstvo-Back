package raf.si.racunovodstvo.knjizenje.reports.schema;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class BilansSchema implements Comparable<BilansSchema> {

    private String konto;
    private String stavki;
    private String naziv;
    private String duguje;
    private String potrazuje;
    private String saldo;

    public BilansSchema(String konto, String stavki, String naziv, String duguje, String potrazuje, String saldo) {
        this.konto = konto;
        this.stavki = stavki;
        this.naziv = naziv;
        this.duguje = duguje;
        this.potrazuje = potrazuje;
        this.saldo = saldo;
    }

    /**
     * Bitan je redosled u listi. Treba nekada unaprediti ove mehanizme.
     * @return
     */
    public List<String> getList() {
        return List.of(konto, stavki, naziv, duguje, potrazuje, saldo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BilansSchema that = (BilansSchema) o;

        return Objects.equals(konto, that.konto);
    }

    @Override
    public int hashCode() {
        return konto != null ? konto.hashCode() : 0;
    }

    @Override
    public int compareTo(BilansSchema o) {
        return konto.compareTo(o.getKonto());
    }
}
