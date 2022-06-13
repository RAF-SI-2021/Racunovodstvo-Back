package raf.si.racunovodstvo.knjizenje.relations;

import raf.si.racunovodstvo.knjizenje.exceptions.OperationNotSupportedException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SifraTransakcijeRelations<T> extends RacunRelations<T> {

    private final Expression<Long> longExpression;
    private final Long longValue;

    public SifraTransakcijeRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        longExpression = root.get(key).get("sifra").as(Long.class);
        longValue = Long.parseLong(val);
    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        return builder.greaterThanOrEqualTo(longExpression, longValue);
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        return builder.lessThanOrEqualTo(longExpression, longValue);
    }

    @Override
    public Predicate equalTo() {
        return builder.equal(longExpression, longValue);
    }
}
