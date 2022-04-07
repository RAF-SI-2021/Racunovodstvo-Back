package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.raf.demo.converter.KnjizenjeConverter;
import rs.raf.demo.model.Knjizenje;
import rs.raf.demo.model.Konto;
import rs.raf.demo.repositories.KnjizenjeRepository;
import rs.raf.demo.responses.KnjizenjeResponse;
import rs.raf.demo.services.IKnjizenjeService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
public class KnjizenjeService implements IKnjizenjeService {

    private final KnjizenjeRepository knjizenjeRepository;
    private final KontoService kontoService;

    @Lazy
    @Autowired
    private KnjizenjeConverter knjizenjeConverter;

    public KnjizenjeService(KnjizenjeRepository knjizenjeRepository, KontoService kontoService) {
        this.knjizenjeRepository = knjizenjeRepository;
        this.kontoService = kontoService;
    }

    @Transactional
    public Knjizenje save(Knjizenje knjizenje) {



        List<Konto> kontoList = knjizenje.getKonto();
        for(Konto konto : kontoList){
            if(!kontoService.findAll().contains(konto)){
                kontoService.save(konto);
            }
        }

        Knjizenje newKnjizenje = null;

        newKnjizenje.setDatumKnjizenja(knjizenje.getDatumKnjizenja());
        newKnjizenje.setBrojNaloga(knjizenje.getBrojNaloga());
        newKnjizenje.setDokument(knjizenje.getDokument());
        newKnjizenje.setKomentar(knjizenje.getKomentar());
        newKnjizenje.setKonto(kontoList);



        return knjizenjeRepository.save(newKnjizenje);
    }

    @Override
    public Optional<Knjizenje> findById(Long id) {
        return knjizenjeRepository.findById(id);
    }

    @Override
    public List<Knjizenje> findAll() {
        return knjizenjeRepository.findAll();
    }

    @Override
    public List<KnjizenjeResponse> findAllKnjizenjeResponse() {
        return knjizenjeConverter.convert(knjizenjeRepository.findAll()).getContent();
    }

    @Override
    public void deleteById(Long id) {
        knjizenjeRepository.deleteById(id);
    }

    @Override
    public Page<KnjizenjeResponse> findAll(Specification<Knjizenje> spec, Pageable pageSort) {
        Page<Knjizenje> page = knjizenjeRepository.findAll(spec, pageSort);
        return knjizenjeConverter.convert(page.getContent());
    }

    @Override
    public Double getSumaPotrazujeZaKnjizenje(Long id) {
        Optional<Knjizenje> optionalKnjizenje = findById(id);
        if (optionalKnjizenje.isPresent()) {
            List<Konto> allKonto = optionalKnjizenje.get().getKonto();
            return allKonto.stream()
                           .map(Konto::getPotrazuje)
                           .filter(Objects::nonNull)
                           .mapToDouble(d -> d)
                           .sum();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Double getSumaDugujeZaKnjizenje(Long id) {
        Optional<Knjizenje> optionalKnjizenje = findById(id);
        if (optionalKnjizenje.isPresent()) {
            List<Konto> allKonto = optionalKnjizenje.get().getKonto();
            return allKonto.stream()
                           .map(Konto::getDuguje)
                           .filter(Objects::nonNull)
                           .mapToDouble(d -> d)
                           .sum();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Double getSaldoZaKnjizenje(Long id) {
        return this.getSumaPotrazujeZaKnjizenje(id) - this.getSumaDugujeZaKnjizenje(id);
    }

}
