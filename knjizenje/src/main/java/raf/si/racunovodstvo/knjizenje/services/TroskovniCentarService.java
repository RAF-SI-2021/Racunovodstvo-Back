package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;
import raf.si.racunovodstvo.knjizenje.repositories.TroskovniCentarRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.ITroskovniCentarService;

import java.util.List;
import java.util.Optional;

@Service
public class TroskovniCentarService implements ITroskovniCentarService {

    private final TroskovniCentarRepository troskovniCentarRepository;

    public TroskovniCentarService(TroskovniCentarRepository troskovniCentarRepository) {
        this.troskovniCentarRepository = troskovniCentarRepository;
    }

    @Override
    public TroskovniCentar save(TroskovniCentar troskovniCentar) {
        return troskovniCentarRepository.save(troskovniCentar);
    }

    @Override
    public Optional<TroskovniCentar> findById(Long id) {
        return troskovniCentarRepository.findById(id);
    }

    @Override
    public List<TroskovniCentar> findAll() {
        return troskovniCentarRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        troskovniCentarRepository.deleteById(id);
    }

    @Override
    public Page<TroskovniCentar> findAll(Pageable sort) {
        return troskovniCentarRepository.findAll(sort);
    }

    @Override
    public TroskovniCentar updateTroskovniCentar(TroskovniCentar troskovniCentar) {
        double ukupanTrosak = 0.0;
        for(Konto k : troskovniCentar.getKontoList()){
            ukupanTrosak += k.getDuguje()-k.getPotrazuje();
        }
        for(TroskovniCentar tc : troskovniCentar.getTroskovniCentarList()){
            ukupanTrosak += tc.getUkupniTrosak();
        }
        troskovniCentar.setUkupniTrosak(ukupanTrosak);
        updateTrosak(troskovniCentar);
        return troskovniCentarRepository.save(troskovniCentar);
    }
    @Override
    public TroskovniCentar addKontosFromKnjizenje(Knjizenje knjizenje, TroskovniCentar troskovniCentar) {
        double ukupanTrosak = troskovniCentar.getUkupniTrosak();
        for(Konto k : knjizenje.getKonto()){
            ukupanTrosak += k.getDuguje()-k.getPotrazuje();
            troskovniCentar.getKontoList().add(k);
        }
        troskovniCentar.setUkupniTrosak(ukupanTrosak);
        updateTrosak(troskovniCentar);
        return troskovniCentarRepository.save(troskovniCentar);
    }

    private void updateTrosak(TroskovniCentar tc){
        TroskovniCentar parent = tc.getParentTroskovniCentar();
        while(parent != null){
            parent.setUkupniTrosak(parent.getUkupniTrosak()+tc.getUkupniTrosak());
            tc = parent;
            troskovniCentarRepository.save(parent);
            parent = tc.getParentTroskovniCentar();
        }
    }
}
