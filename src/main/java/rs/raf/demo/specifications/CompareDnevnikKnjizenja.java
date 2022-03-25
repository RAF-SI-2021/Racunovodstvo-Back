package rs.raf.demo.specifications;

import rs.raf.demo.model.DnevnikKnjizenja;

import java.util.Comparator;

public class CompareDnevnikKnjizenja  implements Comparator<DnevnikKnjizenja> {

    @Override
    public int compare(DnevnikKnjizenja d1, DnevnikKnjizenja d2) {
        return d1.getDatumKnjizenja().compareTo(d2.getDatumKnjizenja());
    }
}
