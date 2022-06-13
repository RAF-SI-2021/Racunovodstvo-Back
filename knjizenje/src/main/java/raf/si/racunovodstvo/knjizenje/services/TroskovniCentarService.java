package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converters.impl.BazniKontoConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.TroskovniCentarConverter;
import raf.si.racunovodstvo.knjizenje.model.*;
import raf.si.racunovodstvo.knjizenje.repositories.BazniKontoRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.repositories.TroskovniCentarRepository;
import raf.si.racunovodstvo.knjizenje.requests.TroskovniCentarRequest;
import raf.si.racunovodstvo.knjizenje.responses.TroskovniCentarResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.ITroskovniCentarService;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TroskovniCentarService implements ITroskovniCentarService {

    private final TroskovniCentarRepository troskovniCentarRepository;
    private final BazniKontoRepository bazniKontoRepository;
    private final KnjizenjeRepository knjizenjeRepository;
    private TroskovniCentarConverter troskovniCentarConverter;
    private BazniKontoConverter bazniKontoConverter;

    public TroskovniCentarService(TroskovniCentarRepository troskovniCentarRepository,
                                  BazniKontoRepository bazniKontoRepository,
                                  KnjizenjeRepository knjizenjeRepository,
                                  TroskovniCentarConverter troskovniCentarConverter,
                                  BazniKontoConverter bazniKontoConverter) {
        this.troskovniCentarRepository = troskovniCentarRepository;
        this.bazniKontoRepository = bazniKontoRepository;
        this.knjizenjeRepository = knjizenjeRepository;
        this.troskovniCentarConverter = troskovniCentarConverter;
        this.bazniKontoConverter = bazniKontoConverter;
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
    public TroskovniCentar updateTroskovniCentar(TroskovniCentarRequest troskovniCentar) {
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarRepository.findById(troskovniCentar.getId());
        if (optionalTroskovniCentar.isPresent()) {
            TroskovniCentar troskovniCentarToSave = optionalTroskovniCentar.get();
            double ukupanTrosak = 0.0;
            if (troskovniCentar.getKontoList() != null) {
                for (BazniKonto k : troskovniCentar.getKontoList()) {
                    k.setBazniCentar(optionalTroskovniCentar.get());
                    bazniKontoRepository.save(k);
                    ukupanTrosak += k.getDuguje() - k.getPotrazuje();
                }
            }
            if (troskovniCentar.getTroskovniCentarList() != null) {
                for (TroskovniCentar tc : troskovniCentar.getTroskovniCentarList()) {
                    ukupanTrosak += tc.getUkupniTrosak();
                }
            }
            optionalTroskovniCentar.get().setUkupniTrosak(ukupanTrosak);
            updateTrosak(optionalTroskovniCentar.get());
            troskovniCentar.setUkupniTrosak(ukupanTrosak);
            updateTrosak(troskovniCentarToSave);
            troskovniCentarToSave.setNaziv(troskovniCentar.getNaziv());
            troskovniCentarToSave.setLokacijaId(troskovniCentar.getLokacijaId());
            troskovniCentarToSave.setOdgovornoLiceId(troskovniCentar.getOdgovornoLiceId());
            List<Long> bazniKontoIdList = troskovniCentar.getKontoList()
                                                         .stream()
                                                         .map(BazniKonto::getBazniKontoId)
                                                         .collect(Collectors.toList());
            bazniKontoRepository.deleteAll(troskovniCentarToSave.getKontoList()
                                                                .stream()
                                                                .filter(bazniKonto -> !bazniKontoIdList.contains(bazniKonto.getBazniKontoId()))
                                                                .collect(Collectors.toList()));
            troskovniCentarToSave.setKontoList(troskovniCentar.getKontoList());
            return troskovniCentarRepository.save(troskovniCentarToSave);

        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public TroskovniCentar addKontosFromKnjizenje(Knjizenje knjizenje, TroskovniCentar troskovniCentar) {
        Optional<Knjizenje> optionalKnjizenje = knjizenjeRepository.findById(knjizenje.getKnjizenjeId());
        if (optionalKnjizenje.isPresent()) {
            double ukupanTrosak = troskovniCentar.getUkupniTrosak();
            if (optionalKnjizenje.get().getKonto() != null) {
                for (Konto k : optionalKnjizenje.get().getKonto()) {
                    BazniKonto bazniKonto = bazniKontoConverter.convert(k);
                    bazniKonto.setBazniCentar(troskovniCentar);
                    bazniKontoRepository.save(bazniKonto);
                    ukupanTrosak += bazniKonto.getDuguje() - bazniKonto.getPotrazuje();
                    troskovniCentar.getKontoList().add(bazniKonto);
                }
            }
            troskovniCentar.setUkupniTrosak(ukupanTrosak);
            updateTrosak(troskovniCentar);
            return troskovniCentarRepository.save(troskovniCentar);

        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<TroskovniCentarResponse> findAllTroskovniCentriResponse() {
        return troskovniCentarConverter.convert(troskovniCentarRepository.findAll()).getContent();
    }

    @Override
    public void deleteBazniKontoById(Long bazniKontoId) {
        bazniKontoRepository.deleteById(bazniKontoId);
    }

    @Override
    public Optional<BazniKonto> findBazniKontoById(Long bazniKontoId) {
        return bazniKontoRepository.findById(bazniKontoId);
    }

    private void updateTrosak(TroskovniCentar tc) {
        TroskovniCentar parent = tc.getParentTroskovniCentar();
        while (parent != null) {
            double ukupanTrosak = 0.0;
            for (BazniKonto k : parent.getKontoList()) {
                ukupanTrosak += k.getDuguje() - k.getPotrazuje();
            }
            parent.setUkupniTrosak(ukupanTrosak + tc.getUkupniTrosak());
            troskovniCentarRepository.save(parent);
            tc = parent;
            parent = tc.getParentTroskovniCentar();
        }
    }
}
