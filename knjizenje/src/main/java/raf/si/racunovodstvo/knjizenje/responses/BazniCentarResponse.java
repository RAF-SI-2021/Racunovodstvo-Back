package raf.si.racunovodstvo.knjizenje.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import raf.si.racunovodstvo.knjizenje.model.BazniCentar;
import raf.si.racunovodstvo.knjizenje.model.Konto;

import java.util.List;

@Data
@AllArgsConstructor
public class BazniCentarResponse {

    BazniCentar bazniCentar;
    List<Konto>  kontoList;

}
