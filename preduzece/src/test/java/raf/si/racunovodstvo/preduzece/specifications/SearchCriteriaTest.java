package raf.si.racunovodstvo.preduzece.specifications;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SearchCriteriaTest {

    @Test
    public void testSameObject() {
        SearchCriteria searchCriteria = new SearchCriteria("key",":", "value");
        assertTrue(searchCriteria.equals(searchCriteria));
    }

    @Test
    public void testDifferentClass() {
        SearchCriteria searchCriteria = new SearchCriteria("key",":", "value");
        assertFalse(searchCriteria.equals(new String()));
    }

    @Test
    public void testNull() {
        SearchCriteria searchCriteria = new SearchCriteria("key",":", "value");
        assertFalse(searchCriteria.equals(null));
    }

    @Test
    public void testNotEqualSearchCriteria() {
        SearchCriteria searchCriteria1 = new SearchCriteria("key",":", "value");
        SearchCriteria searchCriteria2 = new SearchCriteria("key",">", "value");
        assertFalse(searchCriteria1.equals(searchCriteria2));
    }

    @Test
    public void testEqualSearchCriteria() {
        SearchCriteria searchCriteria1 = new SearchCriteria("key",":", "value");
        SearchCriteria searchCriteria2 = new SearchCriteria("key",":", "value");
        assertTrue(searchCriteria1.equals(searchCriteria2));
    }

    @Test
    public void testHashCode() {
        SearchCriteria searchCriteria = new SearchCriteria("key",":", "value");
        assertEquals(Objects.hash(searchCriteria.getKey(), searchCriteria.getOperation(), searchCriteria.getValue()), searchCriteria.hashCode());
    }

}