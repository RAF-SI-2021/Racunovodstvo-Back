package raf.si.racunovodstvo.knjizenje.utils;

public class FakturaUtil {

    private FakturaUtil() {}

    public static Double calculateRabat(Double prodajnaVrednost, Double rabatProcenat) {
        return prodajnaVrednost * (rabatProcenat / 100);
    }

    public static Double calculatePorez(Double prodajnaVrednost, Double rabat, Double porezProcenat) {
        return (prodajnaVrednost - rabat) * (porezProcenat / 100);
    }

    public static Double calculateIznos(Double prodajnaVrednost, Double rabat, Double porez) {
        return prodajnaVrednost - rabat + porez;
    }
}
