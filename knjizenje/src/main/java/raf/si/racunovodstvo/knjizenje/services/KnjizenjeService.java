package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converter.KnjizenjeConverter;
import raf.si.racunovodstvo.knjizenje.model.*;
import raf.si.racunovodstvo.knjizenje.repositories.DokumentRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.requests.KnjizenjeRequest;
import raf.si.racunovodstvo.knjizenje.responses.KnjizenjeResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IKnjizenjeService;
import raf.si.racunovodstvo.knjizenje.services.impl.IProfitniCentarService;
import raf.si.racunovodstvo.knjizenje.services.impl.ITroskovniCentarService;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class KnjizenjeService implements IKnjizenjeService {

    private final KnjizenjeRepository knjizenjeRepository;
    private final DokumentRepository dokumentRepository;

    private final IProfitniCentarService profitniCentarService;

    private final ITroskovniCentarService troskovniCentarService;
    private final KontoService kontoService;

    @Lazy
    @Autowired
    private KnjizenjeConverter knjizenjeConverter;

    public KnjizenjeService(KnjizenjeRepository knjizenjeRepository, DokumentRepository dokumentRepository, IProfitniCentarService profitniCentarService, ITroskovniCentarService troskovniCentarService, KontoService kontoService) {
        this.knjizenjeRepository = knjizenjeRepository;
        this.dokumentRepository = dokumentRepository;
        this.profitniCentarService = profitniCentarService;
        this.troskovniCentarService = troskovniCentarService;
        this.kontoService = kontoService;
    }

    public Knjizenje save(KnjizenjeRequest knjizenje){
        List<Konto> kontoList = knjizenje.getKonto();

        Knjizenje newKnjizenje = new Knjizenje();

        Dokument dokument;
        if(knjizenje.getDokument() != null && dokumentRepository.findByBrojDokumenta(knjizenje.getDokument().getBrojDokumenta()).isPresent()){
            dokument = dokumentRepository.findByBrojDokumenta(knjizenje.getDokument().getBrojDokumenta()).get();
        } else {
            dokument = dokumentRepository.save(knjizenje.getDokument());
        }

        newKnjizenje.setDatumKnjizenja(knjizenje.getDatumKnjizenja());
        newKnjizenje.setBrojNaloga(knjizenje.getBrojNaloga());
        newKnjizenje.setKomentar(knjizenje.getKomentar());
        newKnjizenje.setDokument(dokument);

        newKnjizenje = knjizenjeRepository.save(newKnjizenje);

        for(Konto konto : kontoList){
            if(konto.getKontoId() == null || !kontoService.findById(konto.getKontoId()).isPresent()){
                konto.setKnjizenje(newKnjizenje);
                kontoService.save(konto);
            }
        }

        newKnjizenje.setKonto(kontoList);
        if(knjizenje.getBazniCentarId() != null) {
            Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(knjizenje.getBazniCentarId());
            Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(knjizenje.getBazniCentarId());
            if (optionalTroskovniCentar.isPresent()) {
                troskovniCentarService.addKontosFromKnjizenje(newKnjizenje, optionalTroskovniCentar.get());
            } else if (optionalProfitniCentar.isPresent()) {
                profitniCentarService.addKontosFromKnjizenje(newKnjizenje, optionalProfitniCentar.get());
            }
        }

        return  knjizenjeRepository.save(newKnjizenje);
    }
    @Override
    public Knjizenje save(Knjizenje knjizenje) {

        List<Konto> kontoList = knjizenje.getKonto();

        Knjizenje newKnjizenje = new Knjizenje();

        Dokument dokument;
        if(knjizenje.getDokument() != null && dokumentRepository.findByBrojDokumenta(knjizenje.getDokument().getBrojDokumenta()).isPresent()){
            dokument = dokumentRepository.findByBrojDokumenta(knjizenje.getDokument().getBrojDokumenta()).get();
        } else {
            dokument = dokumentRepository.save(knjizenje.getDokument());
        }

        newKnjizenje.setDatumKnjizenja(knjizenje.getDatumKnjizenja());
        newKnjizenje.setBrojNaloga(knjizenje.getBrojNaloga());
        newKnjizenje.setKomentar(knjizenje.getKomentar());
        newKnjizenje.setDokument(dokument);

        newKnjizenje = knjizenjeRepository.save(newKnjizenje);

        for(Konto konto : kontoList){
            if(konto.getKontoId() == null || !kontoService.findById(konto.getKontoId()).isPresent()){
                konto.setKnjizenje(newKnjizenje);
                kontoService.save(konto);
            }
        }

        newKnjizenje.setKonto(kontoList);

        return  knjizenjeRepository.save(newKnjizenje);
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
    public List<Konto> findKontoByKnjizenjeId(Long knjizenjeId) {
        Optional<Knjizenje> k = knjizenjeRepository.findById(knjizenjeId);
        return k.get().getKonto();
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
