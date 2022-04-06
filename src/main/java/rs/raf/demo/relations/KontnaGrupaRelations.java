package rs.raf.demo.relations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

public class KontnaGrupaRelations<T> extends ForeignKeyRelations<T> {
    public KontnaGrupaRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        idExpression = root.get(key).get("brojKonta").as(Long.class);
    }
}
