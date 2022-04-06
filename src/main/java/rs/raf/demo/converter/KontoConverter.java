package rs.raf.demo.converter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.Konto;
import rs.raf.demo.responses.GlavnaKnjigaResponse;

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
                        konto.getKontnaGrupa().getBrojKonta())
        ).collect(Collectors.toList()));
    }
}
