package raf.si.racunovodstvo.knjizenje.relations;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static java.lang.Long.parseLong;

public class DateRelations<T> extends RacunRelations<T> {

    private final Expression<Date> dateExpression;
    private final Date dateValue;

    public DateRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        dateExpression = root.get(key).as(Date.class);
        long unixSeconds = parseLong(val);
        dateValue = new Date(unixSeconds*1000L);
    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        return this.builder.greaterThanOrEqualTo(dateExpression,dateValue);
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        return this.builder.lessThanOrEqualTo(dateExpression,dateValue);
    }

    @Override
    public Predicate equalTo() {
        return this.builder.equal(dateExpression,dateValue);
    }
}
