package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Faktura;
import rs.raf.demo.model.TipFakture;
import rs.raf.demo.repositories.FakturaRepository;

import java.util.*;

@Service
public class FakturaService {

    private final FakturaRepository fakturaRepository;

    @Autowired
    public FakturaService(FakturaRepository fakturaRepository) {
        this.fakturaRepository = fakturaRepository;
    }

    public List<Faktura> findAll(){
        return fakturaRepository.findAll();
    }

    public List<Faktura> findUlazneFakture(){
        List<Faktura> all = new ArrayList<>();
        all = fakturaRepository.findAll();
        List<Faktura> ulazneFakture = new ArrayList<>();
        for(Faktura f : all){
            if(f.getTipFakture().equals(TipFakture.ULAZNA_FAKTURA)){
                ulazneFakture.add(f);
            }
        }
        return ulazneFakture;
    }

    public List<Faktura> findIzlacneFakture(){
        List<Faktura> all = new ArrayList<>();
        all = fakturaRepository.findAll();
        List<Faktura> izlacneFakture = new ArrayList<>();
        for(Faktura f : all){
            if(f.getTipFakture().equals(TipFakture.IZLAZNA_FAKTURA)){
                izlacneFakture.add(f);
            }
        }
        return izlacneFakture;
    }

    public Optional<Faktura> findById(Long id){
        return fakturaRepository.findById(id);
    }

    public Faktura save(Faktura faktura){
        return fakturaRepository.save(faktura);
    }
}
