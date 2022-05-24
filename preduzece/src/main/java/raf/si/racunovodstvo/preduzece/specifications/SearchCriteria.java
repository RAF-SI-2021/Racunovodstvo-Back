package raf.si.racunovodstvo.preduzece.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
public class SearchCriteria {
    @Getter private String key;
    @Getter private String operation;
    @Getter private Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchCriteria that = (SearchCriteria) o;
        return Objects.equals(key, that.key) && Objects.equals(operation, that.operation) && Objects.equals(value,
                                                                                                            that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, operation, value);
    }
}
