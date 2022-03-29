package rs.raf.demo.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

class ApiUtilTest {

    @Test
    void testResolveSortingAndPaginationWhenSortIsDesc() {
        Integer page = 0;
        Integer size = 50;
        String[] sort = {"-aaa"};

        Pageable ocekivaniRezultat = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "aaa"));

        Pageable vraceniRezultat = ApiUtil.resolveSortingAndPagination(page, size, sort);

        assertEquals(ocekivaniRezultat, vraceniRezultat);
    }

    @Test
    void testResolveSortingAndPaginationWhenSortIsAsc() {
        Integer page = 0;
        Integer size = 50;
        String[] sort = {"+aaa"};

        Pageable ocekivaniRezultat = PageRequest.of(0, 50, Sort.by(Sort.Direction.ASC, "aaa"));

        Pageable vraceniRezultat = ApiUtil.resolveSortingAndPagination(page, size, sort);

        assertEquals(ocekivaniRezultat, vraceniRezultat);
    }

    @Test
    void testResolveSortingAndPaginationWhenPageIsInvalid() {
        Integer page = -1;
        Integer size = 50;
        String[] sort = {"aaa"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ApiUtil.resolveSortingAndPagination(page, size, sort);
        });

        assertEquals("Page index must not be less than zero!", exception.getMessage());
    }

    @Test
    void testResolveSortingAndPaginationWhenSizeIsInvalid() {
        Integer page = 0;
        Integer size = 0;
        String[] sort = {"aaa"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ApiUtil.resolveSortingAndPagination(page, size, sort);
        });

        assertEquals("Page size must not be less than one!", exception.getMessage());
    }
}
