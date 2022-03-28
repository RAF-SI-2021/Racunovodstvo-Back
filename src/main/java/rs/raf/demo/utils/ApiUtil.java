package rs.raf.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;


public class ApiUtil {

    private ApiUtil(){}

    @Value("${api.pagination.page.default}")
    public static final String DEFAULT_PAGE = "0";
    @Value("${api.pagination.page.min}")
    public static final int MIN_PAGE = 0;
    @Value("${api.pagination.size.default}")
    public static final String DEFAULT_SIZE = "50";
    @Value("${api.pagination.size.min}")
    public static final int MIN_SIZE = 1;
    @Value("${api.pagination.size.max}")
    public static final int MAX_SIZE = 100;

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
