package rs.raf.demo.relations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

public class DokumentRelations<T> extends ForeignKeyRelations<T>{
    public DokumentRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        idExpression = root.get(key).get("dokumentId").as(Long.class);
    }
}
