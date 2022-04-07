package rs.raf.demo.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rs.raf.demo.exceptions.OperationNotSupportedException;
import rs.raf.demo.specifications.RacunSpecification;
import rs.raf.demo.specifications.RacunSpecificationsBuilder;

import static org.junit.jupiter.api.Assertions.*;

class SearchUtilTest {

    private SearchUtil searchUtil;

    @BeforeEach
    void setUp() {
        this.searchUtil = new SearchUtil();
    }

    @Test
    public void testHappyPath() {
        String search = "datumKnjizenja>10000";

        RacunSpecification racunSpecification = (RacunSpecification)searchUtil.getSpec(search);
        RacunSpecificationsBuilder builder = new RacunSpecificationsBuilder();
        builder.with("datumKnjizenja",">","10000");
        assertEquals(builder.build(), racunSpecification);
    }

    @Test
    public void testGreaterOrEqualNotSupported() {
        String search = "datumKnjizenja<=10000";

        RacunSpecification racunSpecification = (RacunSpecification)searchUtil.getSpec(search);
        RacunSpecificationsBuilder builder = new RacunSpecificationsBuilder();
        builder.with("datumKnjizenja","<","=10000");
        assertEquals(builder.build(), racunSpecification);
    }

    @Test
    public void testNotEqualNotSupported() {
        String search = "datumKnjizenja!=10000";

        assertThrows(OperationNotSupportedException.class, () -> searchUtil.getSpec(search));
    }

    @Test
    public void testWrongEqualSymbol() {
        String search = "datumKnjizenja=10000";

        assertThrows(OperationNotSupportedException.class, () -> searchUtil.getSpec(search));
    }

    @Test
    public void testEmptySearch() {
        String search = " ";

        assertThrows(OperationNotSupportedException.class, () -> searchUtil.getSpec(search));
    }
}