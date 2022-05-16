package raf.si.racunovodstvo.knjizenje.services;

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
    public TroskovniCentar addKontosIntoTroskovniCentar(Knjizenje knjizejne, TroskovniCentar troskovniCentar) {
        for(Konto k : knjizejne.getKonto()){
            troskovniCentar.getKontoList().add(k);
            troskovniCentar.setUkupniTrosak(troskovniCentar.getUkupniTrosak()+(k.getDuguje()-k.getPotrazuje()));
        }
        updateTrosak(troskovniCentar);
        return troskovniCentar;
    }

    @Override
    public void deleteKontoFromTroskovniCentar(Konto konto, TroskovniCentar troskovniCentar) {
        troskovniCentar.getKontoList().remove(konto);
        for(Konto k : troskovniCentar.getKontoList()){
            troskovniCentar.setUkupniTrosak(troskovniCentar.getUkupniTrosak()+(k.getDuguje()-k.getPotrazuje()));
        }
    }

    @Override
    public void updateKontoInTroskovniCentar(Konto konto, TroskovniCentar troskovniCentar) {
        for(Konto k : troskovniCentar.getKontoList()){
            if(k.getKontoId() == konto.getKontoId()){
                troskovniCentar.setUkupniTrosak(troskovniCentar.getUkupniTrosak()-(k.getDuguje()-k.getPotrazuje())+(konto.getDuguje()- konto.getPotrazuje()));
                troskovniCentar.getKontoList().remove(k);
                troskovniCentar.getKontoList().add(konto);
            }
        }
        updateTrosak(troskovniCentar);
    }

    private void updateTrosak(TroskovniCentar tc){
        if(tc.getParentTroskovniCentar()!= null){
            TroskovniCentar parent = tc.getParentTroskovniCentar();
            do{
                parent.setUkupniTrosak(parent.getUkupniTrosak()+tc.getUkupniTrosak());
                tc = parent;
                parent = tc.getParentTroskovniCentar();
            }while(parent != null);
        }
    }
}
