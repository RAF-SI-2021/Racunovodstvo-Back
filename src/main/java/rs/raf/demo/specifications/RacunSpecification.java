package rs.raf.demo.specifications;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.exceptions.OperationNotSupportedException;
import rs.raf.demo.model.Dokument;
import rs.raf.demo.model.KontnaGrupa;
import rs.raf.demo.model.Preduzece;
import rs.raf.demo.model.enums.TipFakture;
import rs.raf.demo.relations.*;

import javax.persistence.criteria.*;

import java.util.Date;


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
        if (Dokument.class == keyType) {
            return new DokumentRelations<>(root, builder, key, val);
        }
        if (TipFakture.class == keyType) {
            return new TipFaktureRelations<>(root, builder, key, val);
        }
        if (KontnaGrupa.class == keyType) {
            return new KontnaGrupaRelations<>(root, builder, key, val);
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
}
