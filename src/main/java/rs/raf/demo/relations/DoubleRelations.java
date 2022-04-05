package rs.raf.demo.relations;

import javax.persistence.criteria.*;

import static java.lang.Double.parseDouble;

public class DoubleRelations<T> extends RacunRelations<T> {

    private final Expression<Double> doubleExpression;
    private final Double doubleValue;

    public DoubleRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);

        doubleExpression = root.get(key).as(Double.class);
        doubleValue = parseDouble(val);

    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        return this.builder.greaterThanOrEqualTo(doubleExpression,doubleValue);
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        return this.builder.lessThanOrEqualTo(doubleExpression,doubleValue);
    }

    @Override
    public Predicate equalTo() {
        return this.builder.equal(doubleExpression,doubleValue);
    }
}
