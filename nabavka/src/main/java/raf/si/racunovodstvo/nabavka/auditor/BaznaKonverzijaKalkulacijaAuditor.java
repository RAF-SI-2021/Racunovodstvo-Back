package raf.si.racunovodstvo.nabavka.auditor;

import org.springframework.util.CollectionUtils;
import raf.si.racunovodstvo.nabavka.model.BaznaKonverzijaKalkulacija;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class BaznaKonverzijaKalkulacijaAuditor {

    @PrePersist
    @PreUpdate
    private void beforeSave(BaznaKonverzijaKalkulacija baznaKonverzijaKalkulacija) {
        if (!CollectionUtils.isEmpty(baznaKonverzijaKalkulacija.getTroskoviNabavke())) {
            baznaKonverzijaKalkulacija.getTroskoviNabavke()
                                      .forEach(troskoviNabavke -> troskoviNabavke.setBaznaKonverzijaKalkulacija(baznaKonverzijaKalkulacija));
        }
    }
}
