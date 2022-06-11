package raf.si.racunovodstvo.nabavka.relations;

import raf.si.racunovodstvo.nabavka.exceptions.OperationNotSupportedException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class StringRelations<T> extends RacunRelations<T> {

    private final Expression<String> stringExpression;

    public StringRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        stringExpression = root.get(key).as(String.class);
    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        throw new OperationNotSupportedException(String.format("Relacija > nije implementirana za tip string [key:%s,val%s]", key, val));
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        throw new OperationNotSupportedException(String.format("Relacija < nije implementirana za tip string [key:%s,val%s]",key,val));
    }

    @Override
    public Predicate equalTo() {
        return builder.like(stringExpression,"%"+val+"%");
    }
}
