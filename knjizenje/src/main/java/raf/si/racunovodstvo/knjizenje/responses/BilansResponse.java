package raf.si.racunovodstvo.knjizenje.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class BilansResponse {

    private Double duguje;
    private Double potrazuje;
    private String brojKonta;
    private String nazivKonta;
    private Long brojStavki;
    private Double saldo;

    public BilansResponse(Double duguje, Double potrazuje, Long brojStavki, String brojKonta, String nazivKonta) {
        this.duguje = duguje;
        this.potrazuje = potrazuje;
        this.brojKonta = brojKonta;
        this.nazivKonta = nazivKonta;
        this.brojStavki = brojStavki;
        this.saldo = duguje - potrazuje;
    }

    @Override
    public String toString() {
        return "BilansResponse{" +
            "duguje=" + duguje +
            ", potrazuje=" + potrazuje +
            ", brojKonta='" + brojKonta + '\'' +
            ", nazivKonta='" + nazivKonta + '\'' +
            ", brojStavki=" + brojStavki +
            ", saldo=" + saldo +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BilansResponse that = (BilansResponse) o;

        return Objects.equals(that.getBrojKonta(), this.getBrojKonta());
    }

    @Override
    public int hashCode() {
        int result = brojKonta.hashCode();
        result = 31 * result + nazivKonta.hashCode();
        return result;
    }
}
