package rs.raf.demo.specifications;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.model.Faktura;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class FakturaSpecification implements Specification<Faktura> {

    private SearchCriteria criteria;
    private SearchUtil searchUtil;

    @Override
    public Predicate toPredicate
            (Root<Faktura> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            return searchUtil.searchUtilit(criteria, root, query, builder);
    }
}
