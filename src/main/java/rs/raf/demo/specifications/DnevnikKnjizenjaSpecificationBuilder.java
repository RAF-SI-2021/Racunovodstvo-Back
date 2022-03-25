package rs.raf.demo.specifications;

import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.model.DnevnikKnjizenja;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DnevnikKnjizenjaSpecificationBuilder {

    private final List<SearchCriteria> params;

    public DnevnikKnjizenjaSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public DnevnikKnjizenjaSpecificationBuilder with (String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<DnevnikKnjizenja> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<DnevnikKnjizenja>> specs = params.stream()
                .map(DnevnikKnjizenjaSpecification::new)
                .collect(Collectors.toList());

        Specification<DnevnikKnjizenja> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
