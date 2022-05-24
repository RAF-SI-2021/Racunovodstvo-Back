package raf.si.racunovodstvo.knjizenje.converter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.responses.GlavnaKnjigaResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KontoConverter {

    public Page<GlavnaKnjigaResponse> convert(List<Konto> kontoList) {
        return new PageImpl<>(kontoList.stream().map(
            konto -> new GlavnaKnjigaResponse(
                konto.getKnjizenje().getKnjizenjeId(),
                konto.getKnjizenje().getDatumKnjizenja(),
                konto.getPotrazuje(),
                konto.getDuguje(),
                konto.getDuguje() - konto.getPotrazuje(),
                konto.getKontnaGrupa().getNazivKonta(),
                konto.getKontnaGrupa().getBrojKonta(),
                konto.getKnjizenje().getKomentar())
        ).collect(Collectors.toList()));
    }
}
