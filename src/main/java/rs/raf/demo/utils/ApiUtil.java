package rs.raf.demo.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;


public class ApiUtil {
    public static Pageable resolveSortingAndPagination(Integer page, Integer size, String[] sort) {
        List<Order> orders = new ArrayList<>();
        for (String sortParam : sort) {
            Direction sortDir = sortParam.startsWith("-") ? Direction.DESC : Direction.ASC;
            sortParam = sortParam.startsWith("-") || sortParam.startsWith("+") ? sortParam.substring(1) : sortParam;
            orders.add(new Order(sortDir, sortParam));
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }
}
