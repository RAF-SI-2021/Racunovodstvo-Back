package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.constants.RedisConstants;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.converters.impl.KonverzijaConverter;
import raf.si.racunovodstvo.nabavka.converters.impl.KonverzijaRequestConverter;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.model.KonverzijaArtikal;
import raf.si.racunovodstvo.nabavka.model.Lokacija;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.repositories.ArtikalRepository;
import raf.si.racunovodstvo.nabavka.repositories.KonverzijaRepository;
import raf.si.racunovodstvo.nabavka.requests.KonverzijaRequest;
import raf.si.racunovodstvo.nabavka.responses.KonverzijaResponse;
import raf.si.racunovodstvo.nabavka.services.IKonverzijaService;
import raf.si.racunovodstvo.nabavka.services.ILokacijaService;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class KonverzijaService implements IKonverzijaService {

    private final KonverzijaRepository konverzijaRepository;
    private final ILokacijaService lokacijaService;
    private final IConverter<Konverzija, KonverzijaResponse> konverzijaConverter;
    private final IConverter<KonverzijaRequest, Konverzija> konverzijaRequestConverter;
    private final ArtikalRepository artikalRepository;

    @Autowired
    public KonverzijaService(KonverzijaRepository konverzijaRepository,
                             ILokacijaService lokacijaService,
                             KonverzijaConverter konverzijaConverter,
                             KonverzijaRequestConverter konverzijaRequestConverter,
                             ArtikalRepository artikalRepository) {
        this.konverzijaRepository = konverzijaRepository;
        this.lokacijaService = lokacijaService;
        this.konverzijaConverter = konverzijaConverter;
        this.konverzijaRequestConverter = konverzijaRequestConverter;
        this.artikalRepository = artikalRepository;
    }

    @Override
    public Page<KonverzijaResponse> findAll(Specification<Konverzija> spec, Pageable pageSort) {
        Page<Konverzija> page = konverzijaRepository.findAll(spec, pageSort);
        return page.map(konverzijaConverter::convert);
    }

    @Override
    public Page<KonverzijaResponse> findAll(Pageable pageSort) {
        Page<Konverzija> page = konverzijaRepository.findAll(pageSort);
        return page.map(konverzijaConverter::convert);
    }


    @Override
    public <S extends Konverzija> S save(S var1) {
        return konverzijaRepository.save(var1);
    }

    @Override
    public Optional<Konverzija> findById(Long id) {
        return konverzijaRepository.findById(id);
    }

    @Override
    public List<Konverzija> findAll() {
        return konverzijaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        konverzijaRepository.deleteById(id);
    }

    @Override
    public KonverzijaResponse saveKonverzija(KonverzijaRequest konverzijaRequest) {
        Konverzija converted = konverzijaRequestConverter.convert(konverzijaRequest);
        converted.setLokacija(getLokacijaForKonverzija(converted.getLokacija()));
        return konverzijaConverter.convert(konverzijaRepository.save(converted));
    }

    @Override
    public Konverzija increaseNabavnaCena(Long konverzijaId, KonverzijaArtikal artikal) {
        Optional<Konverzija> optionalKonverzija = findById(konverzijaId);
        if (optionalKonverzija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Konverzija konverzija = optionalKonverzija.get();
        Optional<Artikal> optionalArtikal =
            artikal.getArtikalId() != null ? artikalRepository.findById(artikal.getArtikalId()) : Optional.empty();
        double oldFakturnaCena = optionalArtikal.isPresent() ? optionalArtikal.get().getUkupnaNabavnaVrednost() : 0.0;
        Double ukupnaFakturnaCena = konverzija.getFakturnaCena() - oldFakturnaCena + artikal.getUkupnaNabavnaVrednost();
        konverzija.setFakturnaCena(ukupnaFakturnaCena);
        Double ukupniTroskoviNabavke = konverzija.getTroskoviNabavke().stream().mapToDouble(TroskoviNabavke::getCena).sum();
        konverzija.setNabavnaVrednost(ukupniTroskoviNabavke + ukupnaFakturnaCena);
        return konverzijaRepository.save(konverzija);
    }

    private Lokacija getLokacijaForKonverzija(Lokacija lokacija) {
        if (lokacija.getLokacijaId() == null) {
            return lokacijaService.save(lokacija);
        }
        Optional<Lokacija> optionalLokacija = lokacijaService.findById(lokacija.getLokacijaId());
        if (optionalLokacija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return optionalLokacija.get();
    }
}
