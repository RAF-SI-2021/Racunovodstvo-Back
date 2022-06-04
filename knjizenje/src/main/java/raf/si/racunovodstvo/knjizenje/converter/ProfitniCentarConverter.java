package raf.si.racunovodstvo.knjizenje.converter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.responses.BazniCentarResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfitniCentarConverter {

    public Page<BazniCentarResponse> convert(List<ProfitniCentar> profitniCentarList){
        return new PageImpl<>(profitniCentarList.stream().map(
                profitniCentar -> new BazniCentarResponse(
                        profitniCentar.getId(),
                        profitniCentar.getSifra(),
                        profitniCentar.getNaziv(),
                        profitniCentar.getUkupniTrosak(),
                        profitniCentar.getLokacijaId(),
                        profitniCentar.getOdgovornoLiceId(),
                        profitniCentar.getKontoList(),
                        profitniCentar.getParentProfitniCentar()
                )
        ).collect(Collectors.toList()));
    }
}
