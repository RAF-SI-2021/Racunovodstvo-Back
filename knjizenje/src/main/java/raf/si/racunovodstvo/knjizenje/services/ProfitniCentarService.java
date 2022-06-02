package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.repositories.KontoRepository;
import raf.si.racunovodstvo.knjizenje.repositories.ProfitniCentarRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.IProfitniCentarService;

import java.util.List;
import java.util.Optional;

@Service
public class ProfitniCentarService implements IProfitniCentarService {

    private final ProfitniCentarRepository profitniCentarRepository;
    private final KontoRepository kontoRepository;

    public ProfitniCentarService(ProfitniCentarRepository profitniCentarRepository, KontoRepository kontoRepository) {
        this.profitniCentarRepository = profitniCentarRepository;
        this.kontoRepository = kontoRepository;
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
    public Page<ProfitniCentar> findAll(Pageable sort) {
        return profitniCentarRepository.findAll(sort);
    }

    @Override
    public ProfitniCentar updateProfitniCentar(ProfitniCentar profitniCentar) {
        double ukupanProfit = 0.0;
        for(Konto k: profitniCentar.getKontoList()){
            k.setBazniCentar(profitniCentar);
            kontoRepository.save(k);
            ukupanProfit += k.getDuguje()-k.getPotrazuje();
        }
        for(ProfitniCentar pc : profitniCentar.getProfitniCentarList()){
            ukupanProfit += pc.getUkupniTrosak();
        }
        profitniCentar.setUkupniTrosak(ukupanProfit);
        updateProfit(profitniCentar);
        return profitniCentarRepository.save(profitniCentar);
    }

    @Override
    public ProfitniCentar addKontosFromKnjizenje(List<Konto> kontoList, ProfitniCentar profitniCentar) {
        double ukupanProfit = profitniCentar.getUkupniTrosak();
        for(Konto k: kontoList){
            k.setBazniCentar(profitniCentar);
            kontoRepository.save(k);
            ukupanProfit += k.getDuguje()-k.getPotrazuje();
            profitniCentar.getKontoList().add(k);
        }
        profitniCentar.setUkupniTrosak(ukupanProfit);
        updateProfit(profitniCentar);
        return profitniCentarRepository.save(profitniCentar);
    }


    private void updateProfit(ProfitniCentar pc){
        ProfitniCentar parent = pc.getParentProfitniCentar();
        while(parent != null){
                parent.setUkupniTrosak(parent.getUkupniTrosak()+pc.getUkupniTrosak());
                pc = parent;
                profitniCentarRepository.save(parent);
                parent = pc.getParentProfitniCentar();
        }
    }
}
