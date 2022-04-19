package raf.si.racunovodstvo.knjizenje.specifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class SearchCriteriaTest {

    private SearchCriteria searchCriteria;

    private static final String MOCK_KEY = "DUMMY_KEY";
    private static final String MOCK_OPERATION = "DUMMY_OPERATION";
    private static final String MOCK_VALUE = "DUMMY_VALUE";

    @BeforeEach
    void setup() {
        searchCriteria = new SearchCriteria(MOCK_KEY, MOCK_OPERATION, MOCK_VALUE);
    }

    @Test
    void equalsSameObjectSuccessTest() {
        assertEquals(searchCriteria, searchCriteria);
    }

    @Test
    void equalsDifferentObjectsSuccessTest() {
        SearchCriteria that = new SearchCriteria(MOCK_KEY, MOCK_OPERATION, MOCK_VALUE);
        assertEquals(searchCriteria, that);
    }

    @Test
    void equalsDifferentClassFailTest() {
        Object object = new Object();
        assertNotEquals(searchCriteria, object);
    }

    @Test
    void hashCodeTest() {
        assertEquals(Objects.hash(MOCK_KEY, MOCK_OPERATION, MOCK_VALUE), searchCriteria.hashCode());
    }
}
