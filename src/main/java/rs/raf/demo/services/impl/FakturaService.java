package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Faktura;

import rs.raf.demo.model.enums.TipFakture;
import rs.raf.demo.repositories.FakturaRepository;
import rs.raf.demo.services.IFakturaService;
import rs.raf.demo.utils.Utils;

import java.util.*;

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

    public List<Faktura> findUlazneFakture(){
        List<Faktura> ulazneFakture = new ArrayList<>();
        for(Faktura f : fakturaRepository.findAll()){
            if(f.getTipFakture().equals(TipFakture.ULAZNA_FAKTURA)){
                ulazneFakture.add(f);
            }
        }
        return ulazneFakture;
    }

    public List<Faktura> findIzlazneFakture(){
        List<Faktura> izlazneFakture = new ArrayList<>();
        for(Faktura f : fakturaRepository.findAll()){
            if(f.getTipFakture().equals(TipFakture.IZLAZNA_FAKTURA)){
                izlazneFakture.add(f);
            }
        }
        return izlazneFakture;
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
        return fakturaRepository.save(faktura);
    }

    @Override
    public void deleteById(Long id) {
        fakturaRepository.deleteById(id);
    }
}
