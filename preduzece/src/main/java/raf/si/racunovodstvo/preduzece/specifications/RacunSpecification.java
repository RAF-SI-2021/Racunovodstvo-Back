package raf.si.racunovodstvo.preduzece.specifications;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.preduzece.relations.BooleanRelations;
import raf.si.racunovodstvo.preduzece.relations.DateRelations;
import raf.si.racunovodstvo.preduzece.relations.DoubleRelations;
import raf.si.racunovodstvo.preduzece.relations.LongRelations;
import raf.si.racunovodstvo.preduzece.relations.RacunRelations;
import raf.si.racunovodstvo.preduzece.relations.StringRelations;

import java.util.Date;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


@AllArgsConstructor
public class RacunSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;


    private RacunRelations<T> getRelations(Root<T> root, CriteriaBuilder builder, Class keyType, String key, String val)
        throws OperationNotSupportedException {
        if (Date.class == keyType) {
            return new DateRelations<>(root, builder, key, val);
        }
        if (Long.class == keyType) {
            return new LongRelations<>(root, builder, key, val);
        }
        if (String.class == keyType) {
            return new StringRelations<>(root, builder, key, val);
        }
        if (Double.class == keyType) {
            return new DoubleRelations<>(root, builder, key, val);
        }
        if (Boolean.class == keyType) {
            return new BooleanRelations<>(root, builder, key, val);
        }

        throw new OperationNotSupportedException(String.format("Josuvek nije podrzano filtriranje po tipu %s(%s)", key, keyType));
    }

    @Override
    public Predicate toPredicate
            (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        Class keyType = root.get(criteria.getKey()).getJavaType();
        RacunRelations<T> relations = getRelations(root,builder,keyType,criteria.getKey(),criteria.getValue().toString());

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return relations.greaterThanOrEqualTo();
        }
        if (criteria.getOperation().equalsIgnoreCase("<")) {
            return relations.lessThanOrEqualTo();
        }
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            return relations.equalTo();
        }
        throw new OperationNotSupportedException(String.format("Nepoznata operacija \"%s\"",criteria.getOperation()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RacunSpecification<?> that = (RacunSpecification<?>) o;
        return Objects.equals(criteria, that.criteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(criteria);
    }
}