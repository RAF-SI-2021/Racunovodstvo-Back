package raf.si.racunovodstvo.knjizenje.converter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;
import raf.si.racunovodstvo.knjizenje.responses.BazniCentarResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TroskovniCentarConverter {

    public Page<BazniCentarResponse> convert(List<TroskovniCentar> troskovniCentarList){
        return new PageImpl<>(troskovniCentarList.stream().map(
                troskovniCentar -> new BazniCentarResponse(
                        troskovniCentar,troskovniCentar.getKontoList()
                )
        ).collect(Collectors.toList()));
    }
}
