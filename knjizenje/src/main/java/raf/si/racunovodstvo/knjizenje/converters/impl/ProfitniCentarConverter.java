package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.responses.ProfitniCentarResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfitniCentarConverter {

    public Page<ProfitniCentarResponse> convert(List<ProfitniCentar> profitniCentarList){
        return new PageImpl<>(profitniCentarList.stream().map(
                profitniCentar -> new ProfitniCentarResponse(
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
