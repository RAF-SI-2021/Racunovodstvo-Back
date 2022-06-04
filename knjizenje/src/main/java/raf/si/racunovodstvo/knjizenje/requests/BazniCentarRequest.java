package raf.si.racunovodstvo.knjizenje.requests;

import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.BazniCentar;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;

@Data
public class BazniCentarRequest {

    private Knjizenje knjizenje;

    private Long bazniCentarId;
}
