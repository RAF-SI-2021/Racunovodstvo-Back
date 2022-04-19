package raf.si.racunovodstvo.preduzece.specifications;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SearchCriteriaTest {

    @Test
    void testSameObject() {
        SearchCriteria searchCriteria = new SearchCriteria("key",":", "value");
        assertEquals(true, searchCriteria.equals(searchCriteria));
    }

    @Test
    void testDifferentClass() {
        SearchCriteria searchCriteria = new SearchCriteria("key",":", "value");
        assertEquals(false, searchCriteria.equals(new String()));
    }

    @Test
    void testNull() {
        SearchCriteria searchCriteria = new SearchCriteria("key",":", "value");
        assertEquals(false, searchCriteria.equals(null));
    }

    @Test
    void testNotEqualSearchCriteria() {
        SearchCriteria searchCriteria1 = new SearchCriteria("key",":", "value");
        SearchCriteria searchCriteria2 = new SearchCriteria("key",">", "value");
        assertEquals(false, searchCriteria1.equals(searchCriteria2));
    }

    @Test
    void testEqualSearchCriteria() {
        SearchCriteria searchCriteria1 = new SearchCriteria("key",":", "value");
        SearchCriteria searchCriteria2 = new SearchCriteria("key",":", "value");
        assertEquals(true, searchCriteria1.equals(searchCriteria2));
    }

    @Test
    void testHashCode() {
        SearchCriteria searchCriteria = new SearchCriteria("key",":", "value");
        assertEquals(Objects.hash(searchCriteria.getKey(), searchCriteria.getOperation(), searchCriteria.getValue()), searchCriteria.hashCode());
    }

}