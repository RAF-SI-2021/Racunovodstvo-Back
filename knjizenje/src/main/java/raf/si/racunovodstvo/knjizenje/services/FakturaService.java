package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;
import raf.si.racunovodstvo.knjizenje.repositories.FakturaRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.IFakturaService;
import raf.si.racunovodstvo.knjizenje.utils.FakturaUtil;
import raf.si.racunovodstvo.knjizenje.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FakturaService implements IFakturaService {

    private final FakturaRepository fakturaRepository;

    @Autowired
    public FakturaService(FakturaRepository fakturaRepository) {
        this.fakturaRepository = fakturaRepository;
    }

    public List<Faktura> findAll(){
        return fakturaRepository.findAll();
    }

    public List<Faktura> findAll(Specification<Faktura> spec){
        return fakturaRepository.findAll(spec);
    }

    @Override
    public Page<Faktura> findAll(Pageable pageSort) {
        return fakturaRepository.findAll(pageSort);
    }

    @Override
    public Map<String, Double> getSume(String tipFakture) {
        TipFakture tip = TipFakture.valueOf(tipFakture);
        Double sumaPorez = calculateSumPorez(tip);
        Double sumaProdajnaVrednost = calculateSumProdajnaVrednost(tip);
        Double sumaRabat = calculateSumRabat(tip);
        Double sumaZaNaplatu = calculateSumNaplata(tip);

        Map<String, Double> sume = new HashMap<>();
        sume.put("sumaPorez", sumaPorez);
        sume.put("sumaProdajnaVrednost", sumaProdajnaVrednost);
        sume.put("sumaRabat", sumaRabat);
        sume.put("sumaZaNaplatu", sumaZaNaplatu);
        return sume;
    }

    private Double calculateSumPorez(TipFakture tipFakture) {
        List<Double> fakture = fakturaRepository.findPorezForTipFakture(tipFakture);
        return Utils.sum(fakture);
    }

    private Double calculateSumProdajnaVrednost(TipFakture tipFakture) {
        List<Double> fakture = fakturaRepository.findProdajnaVrednostForTipFakture(tipFakture);
        return Utils.sum(fakture);
    }

    private Double calculateSumRabat(TipFakture tipFakture) {
        List<Double> fakture = fakturaRepository.findRabatForTipFakture(tipFakture);
        return Utils.sum(fakture);
    }

    private Double calculateSumNaplata(TipFakture tipFakture) {
        List<Double> fakture = fakturaRepository.findNaplataForTipFakture(tipFakture);
        return Utils.sum(fakture);
    }

    public Optional<Faktura> findById(Long id){
        return fakturaRepository.findByDokumentId(id);
    }

    public Faktura save(Faktura faktura){
        Double prodajnaVrednost = faktura.getProdajnaVrednost();
        Double rabatProcenat = faktura.getRabatProcenat();

        Double porezProcenat = faktura.getPorezProcenat();



        Double rabat = 0.0;
        if(rabatProcenat != null){
            rabat = FakturaUtil.calculateRabat(prodajnaVrednost, rabatProcenat);

        }

        Double porez = 0.0;
        if(porezProcenat != null){
            porez = FakturaUtil.calculatePorez(prodajnaVrednost, rabat, porezProcenat);
        }

        Double iznos = FakturaUtil.calculateIznos(prodajnaVrednost, rabat, porez);

        faktura.setRabat(rabat);
        faktura.setPorez(porez);
        faktura.setIznos(iznos);
        faktura.setTipDokumenta(TipDokumenta.FAKTURA);

        return fakturaRepository.save(faktura);
    }

    public void deleteById(Long id) {
        fakturaRepository.deleteById(id);
    }
}
