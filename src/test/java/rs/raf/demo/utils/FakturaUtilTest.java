package rs.raf.demo.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FakturaUtilTest {

    @Test
    void testCalculateRabat() {
        Double prodajnaVrednost = 11300.0;
        Double rabatProcenat = 5.0;

        Double ocekivaniResultat = 565.0;

        Double vraceniRezultat = FakturaUtil.calculateRabat(prodajnaVrednost, rabatProcenat);

        assertEquals(ocekivaniResultat, vraceniRezultat);
    }

    @Test
    void testCalculatePorez() {
        Double prodajnaVrednost = 11300.0;
        Double rabat = 565.0;
        Double porezProcenat = 20.0;

        Double ocekivaniResultat = 2147.0;

        Double vraceniRezultat = FakturaUtil.calculatePorez(prodajnaVrednost, rabat, porezProcenat);

        assertEquals(ocekivaniResultat, vraceniRezultat);
    }

    @Test
    void testCalculateIznos() {
        Double prodajnaVrednost = 11300.0;
        Double rabat = 565.0;
        Double porez = 2147.0;

        Double ocekivaniResultat = 12882.0;

        Double vraceniRezultat = FakturaUtil.calculateIznos(prodajnaVrednost, rabat, porez);

        assertEquals(ocekivaniResultat, vraceniRezultat);
    }
}