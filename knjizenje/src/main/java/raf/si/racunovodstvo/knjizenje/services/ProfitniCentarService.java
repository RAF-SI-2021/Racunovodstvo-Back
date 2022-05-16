package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.repositories.ProfitniCentarRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.IProfitniCentarService;

import java.util.List;
import java.util.Optional;

@Service
public class ProfitniCentarService implements IProfitniCentarService {

    private final ProfitniCentarRepository profitniCentarRepository;

    public ProfitniCentarService(ProfitniCentarRepository profitniCentarRepository) {
        this.profitniCentarRepository = profitniCentarRepository;
    }

    @Override
    public ProfitniCentar save(ProfitniCentar profitniCentar) {
        return profitniCentarRepository.save(profitniCentar);
    }

    @Override
    public Optional<ProfitniCentar> findById(Long id) {
        return profitniCentarRepository.findById(id);
    }

    @Override
    public List<ProfitniCentar> findAll() {
        return profitniCentarRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        profitniCentarRepository.deleteById(id);
    }


    @Override
    public ProfitniCentar addKontosIntoProfitniCentar(Knjizenje knjizejne, ProfitniCentar profitniCentar) {
        for(Konto k : knjizejne.getKonto()){
            profitniCentar.getKontoList().add(k);
            profitniCentar.setUkupniTrosak(profitniCentar.getUkupniTrosak()+(k.getDuguje()-k.getPotrazuje()));
        }
        updateProfit(profitniCentar);
        return profitniCentar;
    }

    @Override
    public void deleteKontoFromProfitniCentar(Konto konto, ProfitniCentar profitniCentar) {
        profitniCentar.getKontoList().remove(konto);
        for(Konto k : profitniCentar.getKontoList()){
            profitniCentar.setUkupniTrosak(profitniCentar.getUkupniTrosak()+(k.getDuguje()-k.getPotrazuje()));
        }
        updateProfit(profitniCentar);
    }

    @Override
    public void updateKontoInProfitniCentar(Konto konto, ProfitniCentar profitniCentar) {
        for(Konto k : profitniCentar.getKontoList()){
            if(k.getKontoId() == konto.getKontoId()){
                profitniCentar.setUkupniTrosak(profitniCentar.getUkupniTrosak()-(k.getDuguje()-k.getPotrazuje())+(konto.getDuguje()-konto.getPotrazuje()));
                profitniCentar.getKontoList().remove(k);
                profitniCentar.getKontoList().add(konto);
            }
        }
        updateProfit(profitniCentar);
    }


    private void updateProfit(ProfitniCentar pc){
        if(pc.getParentProfitniCentar()!= null){
            ProfitniCentar parent = pc.getParentProfitniCentar();
            do{
                parent.setUkupniTrosak(parent.getUkupniTrosak()+pc.getUkupniTrosak());
                pc = parent;
                parent = pc.getParentProfitniCentar();
            }while(parent != null);
        }
    }
}
