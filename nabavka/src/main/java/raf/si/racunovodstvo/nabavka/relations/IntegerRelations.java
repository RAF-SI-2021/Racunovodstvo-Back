package raf.si.racunovodstvo.nabavka.relations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static java.lang.Integer.parseInt;

public class IntegerRelations<T> extends RacunRelations<T> {

    private final Expression<Integer> integerExpression;
    private final Integer integerValue;

    public IntegerRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);

        integerExpression = root.get(key).as(Integer.class);
        integerValue = parseInt(val);
    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        return this.builder.greaterThanOrEqualTo(integerExpression, integerValue);
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        return this.builder.lessThanOrEqualTo(integerExpression, integerValue);
    }

    @Override
    public Predicate equalTo() {
        return this.builder.equal(integerExpression, integerValue);
    }
}
