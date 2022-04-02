package rs.raf.demo.specifications;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class CustomSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    private Map<String, Method> operationToMethod;

    @SneakyThrows
    public CustomSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
        this.initializeMap();
    }

    private void initializeMap() throws NoSuchMethodException {
        operationToMethod = new HashMap();
        operationToMethod.put(">", CriteriaBuilder.class.getMethod("greaterThanOrEqualTo", Expression.class, Comparable.class));
        operationToMethod.put("<", CriteriaBuilder.class.getMethod("lessThanOrEqualTo", Expression.class, Comparable.class));
        operationToMethod.put(":", CriteriaBuilder.class.getMethod("equal", Expression.class, Object.class));
    }

    @SneakyThrows
    @Override
    public Predicate toPredicate
            (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.initializeMap();
        Expression expression = getCurrentExpression(root);
        return (Predicate) operationToMethod.get(criteria.getOperation())
                .invoke(builder, expression, criteria.getValue().toString());
    }

    private Expression getCurrentExpression(Root<T> root) {
        if (isJoiningRequired(criteria.getKey())) {
            return getExpresionForJoinedTable(root);
        } else {
            return root.get(criteria.getKey());
        }
    }

    private Expression getExpresionForJoinedTable(Root<T> root) {
        String[] tableAndField = criteria.getKey().split("_");
        Join groupJoin = root.join(tableAndField[0]);
        return groupJoin.get(tableAndField[1]);
    }

    private boolean isJoiningRequired(String key) {
        return key.contains("_");
    }
}
