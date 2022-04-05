package rs.raf.demo.relations;

import javax.persistence.criteria.*;

public abstract class RacunRelations<T> {

    protected final Root<T> root;
    protected final CriteriaBuilder builder;
    protected final String key;

    protected String val;

    public RacunRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        this.root = root;
        this.builder = builder;
        this.key = key;
        this.val = val;
    }

    public abstract <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo();
    public abstract <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo();
    public abstract Predicate equalTo();
}
