package rs.raf.demo.utils;

import org.junit.Test;
import rs.raf.demo.model.Koeficijent;
import rs.raf.demo.model.Plata;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlataTest {
    @Test
    public void IzracunavanjeDoprinosa1() {
        Double netoPlata = 30000.0;
        Double ocekivanaBrutoPlata = 6998222.999999999;
        Double ocekivaniDoprinosi1 = 1.4250833769999998E7;
        Double ocekivaniDoprinosi2 = 1.1565375144999998E7;
        Double ocekivaniPorez = 6968222.999999999;
        Double ocekivaniTrosakZarade = 1.8563598144999996E7;

        Koeficijent koeficijent = new Koeficijent();
        koeficijent.setNajnizaOsnovica(30880.0);
        koeficijent.setNajvisaOsnovica(441440.0);
        koeficijent.setPoreskoOslobadjanje(19300.0);
        koeficijent.setKoeficijentPoreza(10.0);
        koeficijent.setPenzionoOsiguranje1(14.0);
        koeficijent.setPenzionoOsiguranje2(11.0);
        koeficijent.setZdravstvenoOsiguranje1(5.15);
        koeficijent.setZdravstvenoOsiguranje2(5.15);
        koeficijent.setNezaposlenost1(0.75);
        koeficijent.setNezaposlenost2(0.0);

        Plata plata = new Plata();
        plata.setNetoPlata(netoPlata);
        plata.izracunajDoprinose(koeficijent);
        assertEquals(plata.getBrutoPlata(), ocekivanaBrutoPlata);
        assertEquals(plata.getDoprinos1(), ocekivaniDoprinosi1);
        assertEquals(plata.getDoprinos2(), ocekivaniDoprinosi2);
        assertEquals(plata.getPorez(), ocekivaniPorez);
        assertEquals(plata.getUkupanTrosakZarade(), ocekivaniTrosakZarade);
    }
    @Test
    public void IzracunavanjeDoprinosa2() {
        Double netoPlata = 150000.0;
        Double ocekivanaBrutoPlata = 2096772.7532097008;
        Double ocekivaniDoprinosi1 = 4258147.7788873045;
        Double ocekivaniDoprinosi2 = 3455732.9964336664;
        Double ocekivaniPorez = 1946772.7532097008;
        Double ocekivaniTrosakZarade = 5552505.749643367;

        Koeficijent koeficijent = new Koeficijent();
        koeficijent.setNajnizaOsnovica(30880.0);
        koeficijent.setNajvisaOsnovica(441440.0);
        koeficijent.setPoreskoOslobadjanje(19300.0);
        koeficijent.setKoeficijentPoreza(10.0);
        koeficijent.setPenzionoOsiguranje1(14.0);
        koeficijent.setPenzionoOsiguranje2(11.0);
        koeficijent.setZdravstvenoOsiguranje1(5.15);
        koeficijent.setZdravstvenoOsiguranje2(5.15);
        koeficijent.setNezaposlenost1(0.75);
        koeficijent.setNezaposlenost2(0.0);

        Plata plata = new Plata();
        plata.setNetoPlata(netoPlata);
        plata.izracunajDoprinose(koeficijent);
        assertEquals(plata.getBrutoPlata(), ocekivanaBrutoPlata);
        assertEquals(plata.getDoprinos1(), ocekivaniDoprinosi1);
        assertEquals(plata.getDoprinos2(), ocekivaniDoprinosi2);
        assertEquals(plata.getPorez(), ocekivaniPorez);
        assertEquals(plata.getUkupanTrosakZarade(), ocekivaniTrosakZarade);
    }
    @Test
    public void IzracunavanjeDoprinosa3() {
        Double netoPlata = 450000.0;
        Double ocekivanaBrutoPlata = 1.0286426744444445E8;
        Double ocekivaniDoprinosi1 = 2.0418846221444443E8;
        Double ocekivaniDoprinosi2 = 1.6571073692277777E8;
        Double ocekivaniPorez = 1.0241426744444445E8;
        Double ocekivaniTrosakZarade = 2.685750043672222E8;

        Koeficijent koeficijent = new Koeficijent();
        koeficijent.setNajnizaOsnovica(30880.0);
        koeficijent.setNajvisaOsnovica(441440.0);
        koeficijent.setPoreskoOslobadjanje(19300.0);
        koeficijent.setKoeficijentPoreza(10.0);
        koeficijent.setPenzionoOsiguranje1(14.0);
        koeficijent.setPenzionoOsiguranje2(11.0);
        koeficijent.setZdravstvenoOsiguranje1(5.15);
        koeficijent.setZdravstvenoOsiguranje2(5.15);
        koeficijent.setNezaposlenost1(0.75);
        koeficijent.setNezaposlenost2(0.0);

        Plata plata = new Plata();
        plata.setNetoPlata(netoPlata);
        plata.izracunajDoprinose(koeficijent);
        assertEquals(plata.getBrutoPlata(), ocekivanaBrutoPlata);
        assertEquals(plata.getDoprinos1(), ocekivaniDoprinosi1);
        assertEquals(plata.getDoprinos2(), ocekivaniDoprinosi2);
        assertEquals(plata.getPorez(), ocekivaniPorez);
        assertEquals(plata.getUkupanTrosakZarade(), ocekivaniTrosakZarade);
    }
}
