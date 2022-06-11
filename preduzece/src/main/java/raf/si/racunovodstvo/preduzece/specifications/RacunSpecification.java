package raf.si.racunovodstvo.preduzece.specifications;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.preduzece.model.Preduzece;
import raf.si.racunovodstvo.preduzece.model.enums.RadnaPozicija;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;
import raf.si.racunovodstvo.preduzece.relations.*;

import java.util.Date;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
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
        if (Preduzece.class == keyType) {
            return new PreduzeceRelations<>(root, builder, key, val);
        }
        if (RadnaPozicija.class == keyType) {
            return new RadnaPozicijaRelations(root, builder, key, val);
        }
        if (StatusZaposlenog.class == keyType) {
            return new StatusZaposlenogRelations(root, builder, key, val);
        }
        if (Boolean.class == keyType) {
            return new BooleanRelations<>(root, builder, key, val);
        }

        throw new OperationNotSupportedException(String.format("Josuvek nije podrzano filtriranje po tipu %s(%s)", key, keyType));
    }

    @Override
    public Predicate toPredicate
        (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        // mgojkovic: Poseban slucaj gde nam treba join kako bismo radili filter po poljima koja nisu kljuc,
        // join radi samo sa equal trenutno (nezavisno od prosledjene operacije), treba generalizovati
        if (isJoiningRequired(criteria.getKey())) {
            Expression exception = getExpresionForJoinedTable(root);
            return builder.equal(exception, criteria.getValue().toString());
        }

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

    private Expression getExpresionForJoinedTable(Root<T> root) {
        String[] tableAndField = criteria.getKey().split("_");
        Join groupJoin = root.join(tableAndField[0]);
        return groupJoin.get(tableAndField[1]);
    }

    private boolean isJoiningRequired(String key) {
        return key.contains("_");
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
