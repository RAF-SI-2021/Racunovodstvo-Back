package raf.si.racunovodstvo.knjizenje.relations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static java.lang.Long.parseLong;

public abstract class ForeignKeyRelations<T> extends RacunRelations<T> {

    protected Expression<Long> idExpression;
    protected final Long id;

    public ForeignKeyRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        id = parseLong(val);
    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        return this.builder.greaterThanOrEqualTo(idExpression,id);
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        return this.builder.lessThanOrEqualTo(idExpression,id);
    }

    @Override
    public Predicate equalTo() {
        return this.builder.equal(idExpression,id);
    }
}
