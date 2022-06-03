package raf.si.racunovodstvo.knjizenje.requests;

import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;

import java.util.List;

@Data
public class BazniCentarRequest {

    private List<Konto> kontoList;

    private ProfitniCentar profitniCentar;
}
