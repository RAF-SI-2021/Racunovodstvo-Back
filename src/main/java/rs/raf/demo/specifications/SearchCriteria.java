package rs.raf.demo.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SearchCriteria {
    @Getter private String key;
    @Getter private String operation;
    @Getter private Object value;

}
