package rs.raf.demo.utils;


import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.exceptions.OperationNotSupportedException;
import rs.raf.demo.specifications.RacunSpecificationsBuilder;

import java.util.Arrays;
import java.util.List;

public class SearchUtil<T> {

    public Specification<T> getSpec(String queryString) {

        List<String> operations = Arrays.asList(">","<",":");
        String regexOperations = String.join("|",operations);

        RacunSpecificationsBuilder<T> builder = new RacunSpecificationsBuilder<>();


        String[] conditions = queryString.split(",");
        for(String condition:conditions){
            String[] matches = condition.split(String.format("((?=%s)|(?<=%s))",regexOperations,regexOperations));

            if(matches.length != 3 || !operations.contains(matches[1])){
                throw new OperationNotSupportedException("Lose formatiran uslova "+ condition);
            }

            builder.with(matches[0],matches[1],matches[2]);
        }

        return builder.build();
    }
}