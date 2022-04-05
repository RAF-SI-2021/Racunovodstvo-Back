package rs.raf.demo.relations;

import rs.raf.demo.exceptions.OperationNotSupportedException;

import javax.persistence.criteria.*;

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
