package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.constants.RedisConstants;
import raf.si.racunovodstvo.knjizenje.converter.KnjizenjeConverter;
import raf.si.racunovodstvo.knjizenje.model.Dokument;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.repositories.DokumentRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.responses.KnjizenjeResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IKnjizenjeService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class KnjizenjeService implements IKnjizenjeService {

    private final KnjizenjeRepository knjizenjeRepository;
    private final DokumentRepository dokumentRepository;
    private final KontoService kontoService;

    @Lazy
    @Autowired
    private KnjizenjeConverter knjizenjeConverter;

    public KnjizenjeService(KnjizenjeRepository knjizenjeRepository,
                            DokumentRepository dokumentRepository,
                            KontoService kontoService) {
        this.knjizenjeRepository = knjizenjeRepository;
        this.dokumentRepository = dokumentRepository;
        this.kontoService = kontoService;
    }

    @Override
    @Caching(
        put = @CachePut(value = RedisConstants.KNJIZENJE_CACHE, key = "#knjizenje.knjizenjeId"),
        evict = {
            @CacheEvict(value = RedisConstants.SUMA_POTRAZUJE_CACHE, key = "#knjizenje.knjizenjeId"),
            @CacheEvict(value = RedisConstants.SUMA_DUGUJE_CACHE, key = "#knjizenje.knjizenjeId")
        })
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
    @Cacheable(value = RedisConstants.KNJIZENJE_CACHE, key = "#id")
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
    @Caching(evict = {
        @CacheEvict(value = RedisConstants.KNJIZENJE_CACHE, key = "#id"),
        @CacheEvict(value = RedisConstants.SUMA_POTRAZUJE_CACHE, key = "#id"),
        @CacheEvict(value = RedisConstants.SUMA_DUGUJE_CACHE, key = "#id")
    })
    public void deleteById(Long id) {
        knjizenjeRepository.deleteById(id);
    }

    @Override
    public Page<KnjizenjeResponse> findAll(Specification<Knjizenje> spec, Pageable pageSort) {
        Page<Knjizenje> page = knjizenjeRepository.findAll(spec, pageSort);
        return knjizenjeConverter.convert(page.getContent());
    }

    @Override
    @Cacheable(value = RedisConstants.SUMA_POTRAZUJE_CACHE, key = "#id")
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
    @Cacheable(value = RedisConstants.SUMA_DUGUJE_CACHE, key = "#id")
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
