package raf.si.racunovodstvo.knjizenje.relations;

import raf.si.racunovodstvo.knjizenje.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TipTransakcijeRelations<T> extends RacunRelations<T>{

    private final Expression<TipTransakcije> tipTransakcijeExpression;
    private final TipTransakcije tipTransakcijeValue;

    public TipTransakcijeRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        tipTransakcijeExpression = root.get("tipTransakcije").as(TipTransakcije.class);
        tipTransakcijeValue = TipTransakcije.valueOf(val);
    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        throw new OperationNotSupportedException(String.format("Relacija > nije implementirana za TipTransakcije [key:%s,val%s]", key, val));
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        throw new OperationNotSupportedException(String.format("Relacija < nije implementirana za TipTransakcije [key:%s,val%s]",key,val));
    }

    @Override
    public Predicate equalTo() {
        return this.builder.equal(tipTransakcijeExpression,tipTransakcijeValue);
    }
}
