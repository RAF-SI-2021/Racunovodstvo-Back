package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converters.impl.BazniKontoConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.ProfitniCentarConverter;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.repositories.BazniKontoRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.repositories.ProfitniCentarRepository;
import raf.si.racunovodstvo.knjizenje.requests.ProfitniCentarRequest;
import raf.si.racunovodstvo.knjizenje.responses.ProfitniCentarResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IProfitniCentarService;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfitniCentarService implements IProfitniCentarService {

    private final ProfitniCentarRepository profitniCentarRepository;
    private final KnjizenjeRepository knjizenjeRepository;
    private final BazniKontoRepository bazniKontoRepository;
    private BazniKontoConverter bazniKontoConverter;
    @Lazy
    private ProfitniCentarConverter profitniCentarConverter;

    public ProfitniCentarService(ProfitniCentarRepository profitniCentarRepository,
                                 KnjizenjeRepository knjizenjeRepository,
                                 BazniKontoRepository bazniKontoRepository,
                                 ProfitniCentarConverter profitniCentarConverter,
                                 BazniKontoConverter bazniKontoConverter) {
        this.profitniCentarRepository = profitniCentarRepository;
        this.knjizenjeRepository = knjizenjeRepository;
        this.bazniKontoRepository = bazniKontoRepository;
        this.profitniCentarConverter = profitniCentarConverter;
        this.bazniKontoConverter = bazniKontoConverter;
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
    public ProfitniCentar updateProfitniCentar(ProfitniCentarRequest profitniCentar) {
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarRepository.findById(profitniCentar.getId());
        if (optionalProfitniCentar.isPresent()) {
            ProfitniCentar profitniCentarToSave = optionalProfitniCentar.get();
            double ukupanProfit = 0.0;
            if (profitniCentar.getKontoList() != null) {
                for (BazniKonto k : profitniCentar.getKontoList()) {
                    k.setBazniCentar(profitniCentarToSave);
                    bazniKontoRepository.save(k);
                    ukupanProfit += k.getDuguje() - k.getPotrazuje();
                }
            }
            if (profitniCentar.getProfitniCentarList() != null) {
                for (ProfitniCentar pc : profitniCentar.getProfitniCentarList()) {
                    ukupanProfit += pc.getUkupniTrosak();
                }
            }
            profitniCentarToSave.setUkupniTrosak(ukupanProfit);
            updateProfit(profitniCentarToSave);
            profitniCentarToSave.setNaziv(profitniCentar.getNaziv());
            profitniCentarToSave.setLokacijaId(profitniCentar.getLokacijaId());
            profitniCentarToSave.setOdgovornoLiceId(profitniCentar.getOdgovornoLiceId());
            List<Long> bazniKontoIdList = profitniCentar.getKontoList()
                                                        .stream()
                                                        .map(BazniKonto::getBazniKontoId)
                                                        .collect(Collectors.toList());
            bazniKontoRepository.deleteAll(profitniCentarToSave.getKontoList()
                                                               .stream()
                                                               .filter(bazniKonto -> !bazniKontoIdList.contains(bazniKonto.getBazniKontoId()))
                                                               .collect(Collectors.toList()));
            profitniCentarToSave.setKontoList(profitniCentar.getKontoList());
            return profitniCentarRepository.save(profitniCentarToSave);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public ProfitniCentar addKontosFromKnjizenje(Knjizenje knjizenje, ProfitniCentar profitniCentar) {
        Optional<Knjizenje> optionalKnjizenje = knjizenjeRepository.findById(knjizenje.getKnjizenjeId());
        if (optionalKnjizenje.isPresent()) {
            double ukupanProfit = profitniCentar.getUkupniTrosak();
            if (optionalKnjizenje.get().getKonto() != null) {
                for (Konto k : optionalKnjizenje.get().getKonto()) {
                    BazniKonto bazniKonto = bazniKontoConverter.convert(k);
                    bazniKonto.setBazniCentar(profitniCentar);
                    bazniKontoRepository.save(bazniKonto);
                    ukupanProfit += k.getDuguje() - k.getPotrazuje();
                    profitniCentar.getKontoList().add(bazniKonto);
                }
            }
            profitniCentar.setUkupniTrosak(ukupanProfit);
            updateProfit(profitniCentar);
            return profitniCentarRepository.save(profitniCentar);

        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<ProfitniCentarResponse> findAllProfitniCentarResponse() {
        return profitniCentarConverter.convert(profitniCentarRepository.findAll()).getContent();
    }

    @Override
    public void deleteBazniKontoById(Long bazniKontoId) {
        bazniKontoRepository.deleteById(bazniKontoId);
    }

    @Override
    public Optional<BazniKonto> findBazniKontoById(Long bazniKontoId) {
        return bazniKontoRepository.findById(bazniKontoId);
    }


    private void updateProfit(ProfitniCentar pc) {
        ProfitniCentar parent = pc.getParentProfitniCentar();
        while (parent != null) {
            double ukupanProfit = 0.0;
            for (BazniKonto k : parent.getKontoList()) {
                ukupanProfit += k.getDuguje() - k.getPotrazuje();
            }
            parent.setUkupniTrosak(ukupanProfit + pc.getUkupniTrosak());
            profitniCentarRepository.save(parent);
            pc = parent;
            parent = pc.getParentProfitniCentar();
        }
    }
}
