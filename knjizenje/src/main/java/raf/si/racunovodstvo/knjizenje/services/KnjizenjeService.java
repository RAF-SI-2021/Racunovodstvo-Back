package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converter.IConverter;
import raf.si.racunovodstvo.knjizenje.converter.impl.AnalitickaKarticaConverter;
import raf.si.racunovodstvo.knjizenje.converter.impl.KnjizenjeConverter;
import raf.si.racunovodstvo.knjizenje.model.Dokument;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.repositories.DokumentRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.responses.AnalitickaKarticaResponse;
import raf.si.racunovodstvo.knjizenje.responses.KnjizenjeResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IKnjizenjeService;


import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

@Service
public class KnjizenjeService implements IKnjizenjeService {

    private final KnjizenjeRepository knjizenjeRepository;
    private final DokumentRepository dokumentRepository;
    private final KontoService kontoService;
    private final IConverter<Knjizenje, AnalitickaKarticaResponse> analitickaKarticaConverter;

    private KnjizenjeConverter knjizenjeConverter;

    public KnjizenjeService(KnjizenjeRepository knjizenjeRepository,
                            DokumentRepository dokumentRepository,
                            KontoService kontoService,
                            AnalitickaKarticaConverter analitickaKarticaConverter, KnjizenjeConverter knjizenjeConverter) {
        this.knjizenjeRepository = knjizenjeRepository;
        this.dokumentRepository = dokumentRepository;
        this.kontoService = kontoService;
        this.analitickaKarticaConverter = analitickaKarticaConverter;
        this.knjizenjeConverter = knjizenjeConverter;
    }

    @Override
    public Knjizenje save(Knjizenje knjizenje) {

        List<Konto> kontoList = knjizenje.getKonto();

        Knjizenje newKnjizenje = new Knjizenje();

        Dokument dokument;
        if (knjizenje.getDokument() != null && dokumentRepository.findByBrojDokumenta(knjizenje.getDokument().getBrojDokumenta())
                                                                 .isPresent()) {
            dokument = dokumentRepository.findByBrojDokumenta(knjizenje.getDokument().getBrojDokumenta()).get();
        } else {
            dokument = dokumentRepository.save(knjizenje.getDokument());
        }

        newKnjizenje.setDatumKnjizenja(knjizenje.getDatumKnjizenja());
        newKnjizenje.setBrojNaloga(knjizenje.getBrojNaloga());
        newKnjizenje.setKomentar(knjizenje.getKomentar());
        newKnjizenje.setDokument(dokument);

        newKnjizenje = knjizenjeRepository.save(newKnjizenje);

        for (Konto konto : kontoList) {
            if (konto.getKontoId() == null || !kontoService.findById(konto.getKontoId()).isPresent()) {
                konto.setKnjizenje(newKnjizenje);
                kontoService.save(konto);
            }
        }

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
    public Page<AnalitickaKarticaResponse> findAllAnalitickeKarticeResponse(Pageable pageSort,
                                                                            String brojKonta,
                                                                            Date datumOd,
                                                                            Date datumDo,
                                                                            Long komitentId) {
        Page<Knjizenje> page = knjizenjeRepository.findAllByBrojKontaAndKomitentId(pageSort, brojKonta, komitentId, datumOd, datumDo);
        return page.map(knjizenje -> {
            knjizenje.setKonto(knjizenje.getKonto()
                                        .stream()
                                        .filter(konto -> konto.getKontnaGrupa().getBrojKonta().equals(brojKonta))
                                        .collect(Collectors.toList()));
            return analitickaKarticaConverter.convert(knjizenje);
        });
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
