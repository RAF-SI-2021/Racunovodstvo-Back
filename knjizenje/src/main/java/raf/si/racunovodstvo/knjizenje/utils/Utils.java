package raf.si.racunovodstvo.knjizenje.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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


    public static String periodToString(Date datumOd, Date datumDo){
        DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        return String.format("%s-%s",dateFormat.format(datumOd),dateFormat.format(datumDo));

    }
}
