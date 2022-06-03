package raf.si.racunovodstvo.knjizenje.requests;

import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;

@Data
public class BazniCentarRequest {

    private Knjizenje knjizenje;

    private ProfitniCentar profitniCentar;
}
