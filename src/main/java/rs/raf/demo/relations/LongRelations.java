package rs.raf.demo.relations;

import javax.persistence.criteria.*;

import static java.lang.Long.parseLong;

public class LongRelations<T> extends RacunRelations<T> {

    private final Expression<Long> longExpression;
    private final Long longValue;

    public LongRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);

        longExpression = root.get(key).as(Long.class);
        longValue = parseLong(val);

    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        return this.builder.greaterThanOrEqualTo(longExpression,longValue);
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        return this.builder.lessThanOrEqualTo(longExpression,longValue);
    }

    @Override
    public Predicate equalTo() {
        return this.builder.equal(longExpression,longValue);
    }
}
