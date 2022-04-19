package raf.si.racunovodstvo.preduzece.relations;

import raf.si.racunovodstvo.preduzece.exceptions.OperationNotSupportedException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BooleanRelations<T> extends RacunRelations<T>{

    private final Expression<Boolean> booleanExpression;
    private final Boolean booleanValue;

    public BooleanRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        booleanExpression = root.get(key).as(Boolean.class);
        booleanValue = Boolean.parseBoolean(val);
    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        throw new OperationNotSupportedException(String.format("Relacija > nije implementirana za tip boolean [key:%s,val%s]", key, val));
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        throw new OperationNotSupportedException(String.format("Relacija < nije implementirana za tip boolean [key:%s,val%s]", key, val));
    }

    @Override
    public Predicate equalTo() {
        return this.builder.equal(booleanExpression, booleanValue);
    }
}
