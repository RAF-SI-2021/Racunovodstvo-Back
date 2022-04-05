package rs.raf.demo.relations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

public class PreduzeceRelations<T> extends ForeignKeyRelations<T>{
    public PreduzeceRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        idExpression = root.get(key).get("preduzeceId").as(Long.class);
    }
}
