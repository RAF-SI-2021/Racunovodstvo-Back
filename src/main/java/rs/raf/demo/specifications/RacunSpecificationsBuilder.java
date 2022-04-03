package rs.raf.demo.specifications;

import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.model.Faktura;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RacunSpecificationsBuilder<T> {

    private final List<SearchCriteria> params;

    public RacunSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public RacunSpecificationsBuilder<T> with (String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<T>> specs = params.stream()
                .map(RacunSpecification<T>::new)
                .collect(Collectors.toList());

        Specification<T> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}