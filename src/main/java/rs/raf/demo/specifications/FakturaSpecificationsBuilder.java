package rs.raf.demo.specifications;

import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.model.Faktura;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FakturaSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public FakturaSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public FakturaSpecificationsBuilder with (String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Faktura> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Faktura>> specs = params.stream()
                .map(FakturaSpecification::new)
                .collect(Collectors.toList());

        Specification<Faktura> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}