package raf.si.racunovodstvo.nabavka.utils;

import java.util.List;

public class Utils {

    private Utils() {}

    /**
     * @param args Argumenti za sumiranje.
     * @return Suma Double-ova, s tim sto null vrednosti u listi preskace.
     */
    public static Double sum(List<Double> args) {
        return args.stream().reduce(0.0, Double::sum);
    }
}
