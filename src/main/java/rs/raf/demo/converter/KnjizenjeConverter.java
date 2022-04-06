package rs.raf.demo.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.Knjizenje;
import rs.raf.demo.responses.KnjizenjeResponse;
import rs.raf.demo.services.IKnjizenjeService;

import java.util.ArrayList;
import java.util.List;

@Component
public class KnjizenjeConverter {

    @Autowired
    private IKnjizenjeService knjizenjeService;

    public Page convert(List<Knjizenje> knjizenja) {
        List<KnjizenjeResponse> responses = new ArrayList<>();
        for (Knjizenje currKnjizenje : knjizenja) {
            KnjizenjeResponse response = new KnjizenjeResponse();

            if (currKnjizenje.getDokument() != null) {
                response.setDokumentId(currKnjizenje.getDokument().getDokumentId());
            }

            response.setKnjizenjeId(currKnjizenje.getKnjizenjeId());
            response.setDatumKnjizenja(currKnjizenje.getDatumKnjizenja());
            response.setBrojNaloga(currKnjizenje.getBrojNaloga());
            response.setKomentar(currKnjizenje.getKomentar());
            response.setDuguje(knjizenjeService.getSumaDugujeZaKnjizenje(currKnjizenje.getKnjizenjeId()));
            response.setPotrazuje(knjizenjeService.getSumaPotrazujeZaKnjizenje(currKnjizenje.getKnjizenjeId()));
            response.setSaldo(knjizenjeService.getSaldoZaKnjizenje(currKnjizenje.getKnjizenjeId()));
            responses.add(response);
        }
        return new PageImpl(responses);
    }
}
