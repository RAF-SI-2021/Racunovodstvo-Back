package raf.si.racunovodstvo.nabavka.services.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.nabavka.constants.RedisConstants;
import raf.si.racunovodstvo.nabavka.converters.IConverter;
import raf.si.racunovodstvo.nabavka.converters.impl.KalkulacijaConverter;
import raf.si.racunovodstvo.nabavka.converters.impl.KalkulacijaReverseConverter;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.model.KalkulacijaArtikal;
import raf.si.racunovodstvo.nabavka.repositories.ArtikalRepository;
import raf.si.racunovodstvo.nabavka.repositories.KalkulacijaRepository;
import raf.si.racunovodstvo.nabavka.requests.KalkulacijaRequest;
import raf.si.racunovodstvo.nabavka.responses.KalkulacijaResponse;
import raf.si.racunovodstvo.nabavka.services.IKalkulacijaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

@Service
public class KalkulacijaService implements IKalkulacijaService {

    private final KalkulacijaRepository kalkulacijaRepository;
    private final IConverter<KalkulacijaRequest, Kalkulacija> kalkulacijaConverter;
    private final IConverter<Kalkulacija, KalkulacijaResponse> kalkulacijaReverseConverter;
    private final ArtikalRepository artikalRepository;

    public KalkulacijaService(KalkulacijaRepository kalkulacijaRepository,
                              KalkulacijaConverter kalkulacijaConverter,
                              KalkulacijaReverseConverter kalkulacijaReverseConverter,
                              ArtikalRepository artikalRepository) {
        this.kalkulacijaRepository = kalkulacijaRepository;
        this.kalkulacijaConverter = kalkulacijaConverter;
        this.kalkulacijaReverseConverter = kalkulacijaReverseConverter;
        this.artikalRepository = artikalRepository;
    }

    @Override
    public Page<KalkulacijaResponse> findAll(Specification<Kalkulacija> spec, Pageable pageSort) {
        return kalkulacijaRepository.findAll(spec, pageSort).map(kalkulacijaReverseConverter::convert);
    }

    @Override
    public Page<Kalkulacija> findAllKalkulacije(Specification<Kalkulacija> spec, Pageable pageable) {
        return kalkulacijaRepository.findAll(spec, pageable);
    }

    @Override
    public Kalkulacija increaseNabavnaAndProdajnaCena(Long kalkulacijaId, KalkulacijaArtikal artikal) {
        Optional<Kalkulacija> optionalKalkulacija = findById(kalkulacijaId);
        if (optionalKalkulacija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Kalkulacija kalkulacija = optionalKalkulacija.get();
        Optional<Artikal> optionalArtikal =
            artikal.getArtikalId() != null ? artikalRepository.findById(artikal.getArtikalId()) : Optional.empty();
        double oldFakturnaCena = 0.0;
        double oldProdajnaCena = 0.0;
        if (optionalArtikal.isPresent()) {
            KalkulacijaArtikal kalkulacijaArtikal = (KalkulacijaArtikal) optionalArtikal.get();
            oldFakturnaCena = kalkulacijaArtikal.getUkupnaNabavnaVrednost();
            oldProdajnaCena = kalkulacijaArtikal.getProdajnaCena();
        }
        kalkulacija.setProdajnaCena(kalkulacija.getProdajnaCena() - oldProdajnaCena + artikal.getProdajnaCena());
        Double ukupnaFakturnaCena = kalkulacija.getFakturnaCena() - oldFakturnaCena + artikal.getUkupnaProdajnaVrednost();
        kalkulacija.setFakturnaCena(ukupnaFakturnaCena);
        Double ukupniTroskoviNabavke = kalkulacija.getTroskoviNabavke().stream().mapToDouble(TroskoviNabavke::getCena).sum();
        kalkulacija.setNabavnaVrednost(ukupniTroskoviNabavke + ukupnaFakturnaCena);
        return kalkulacijaRepository.save(kalkulacija);
    }

    @Override
    public <S extends Kalkulacija> S save(S var1) {
        return kalkulacijaRepository.save(var1);
    }

    @Override
    @CachePut(value = RedisConstants.KALKULACIJA_CACHE, key = "#result.id")
    public KalkulacijaResponse save(KalkulacijaRequest kalkulacijaRequest) {
        Kalkulacija kalkulacija = kalkulacijaConverter.convert(kalkulacijaRequest);
        kalkulacija.calculateCene();
        return kalkulacijaReverseConverter.convert(kalkulacijaRepository.save(kalkulacija));
    }

    @Override
    @CachePut(value = RedisConstants.KALKULACIJA_CACHE, key = "#result.id")
    public KalkulacijaResponse update(KalkulacijaRequest kalkulacijaRequest) {
        Optional<KalkulacijaResponse> optionalKalkulacija = findKalkulacijaById(kalkulacijaRequest.getId());
        if (optionalKalkulacija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return this.save(kalkulacijaRequest);
    }

    @Override
    @Cacheable(value = RedisConstants.KALKULACIJA_CACHE, key = "#id")
    public Optional<KalkulacijaResponse> findKalkulacijaById(Long id) {
        return kalkulacijaRepository.findById(id).map(kalkulacijaReverseConverter::convert);
    }

    @Override
    public Optional<Kalkulacija> findById(Long var1) {
        return kalkulacijaRepository.findById(var1);
    }

    @Override
    public List<Kalkulacija> findAll() {
        return this.kalkulacijaRepository.findAll();
    }

    public Page<KalkulacijaResponse> findAll(Pageable pageable) {
        return kalkulacijaRepository.findAll(pageable).map(kalkulacijaReverseConverter::convert);
    }

    @Override
    @CacheEvict(value = RedisConstants.KALKULACIJA_CACHE, key = "#var1")
    public void deleteById(Long var1) {
        Optional<KalkulacijaResponse> optionalKalkulacija = findKalkulacijaById(var1);
        if (optionalKalkulacija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        kalkulacijaRepository.deleteById(var1);
    }

    @Override
    public Map<String, Number> getTotalKalkulacije(List<Kalkulacija> kalkulacije) {
        Map<String, Number> values = new HashMap<>();
        Integer totalKolicina = 0;
        Double totalRabat = 0.0;
        Double totalNabavnaCena = 0.0;
        Double totalNabavnaCenaPosleRabata = 0.0;
        Double totalNabavnaVrednost = 0.0;
        Double totalMarza = 0.0;
        Double totalOsnovicaZaProdaju = 0.0;
        Double totalPorez = 0.0;
        Double totalProdajnaCena = 0.0;
        Double totalPoreskaOsnovica = 0.0;
        Double totalProdajnaVrednost = 0.0;
        for (Kalkulacija kalkulacija : kalkulacije) {
            Stream<KalkulacijaArtikal> artikliStream = kalkulacija.getArtikli().stream();

            totalKolicina += artikliStream.map(Artikal::getKolicina).reduce(0, Integer::sum);
            totalRabat += artikliStream.map(Artikal::getRabat).reduce(0.0, Double::sum);
            totalNabavnaCena += artikliStream.map(Artikal::getNabavnaCena).reduce(0.0, Double::sum);
            totalNabavnaCenaPosleRabata += artikliStream.map(Artikal::getNabavnaCenaPosleRabata).reduce(0.0, Double::sum);
            totalNabavnaVrednost += artikliStream.map(Artikal::getUkupnaNabavnaVrednost).reduce(0.0, Double::sum);
            totalMarza += artikliStream.map(KalkulacijaArtikal::getMarza).reduce(0.0, Double::sum);
            totalOsnovicaZaProdaju += artikliStream.map(KalkulacijaArtikal::getProdajnaOsnovica).reduce(0.0, Double::sum);
            totalPorez += artikliStream.map(KalkulacijaArtikal::getPorez).reduce(0.0, Double::sum);
            totalProdajnaCena += artikliStream.map(KalkulacijaArtikal::getProdajnaCena).reduce(0.0, Double::sum);
            totalPoreskaOsnovica += artikliStream.map(KalkulacijaArtikal::getOsnovica).reduce(0.0, Double::sum);
            totalProdajnaVrednost += artikliStream.map(KalkulacijaArtikal::getUkupnaProdajnaVrednost).reduce(0.0, Double::sum);
        }
        values.put("totalKolicina", totalKolicina);
        values.put("totalRabat", totalRabat);
        values.put("totalNabavnaCena", totalNabavnaCena);
        values.put("totalNabavnaCenaPosleRabata", totalNabavnaCenaPosleRabata);
        values.put("totalNabavnaVrednost", totalNabavnaVrednost);
        values.put("totalMarza", totalMarza);
        values.put("totalOsnovicaZaProdaju", totalOsnovicaZaProdaju);
        values.put("totalPorez", totalPorez);
        values.put("totalProdajnaCena", totalProdajnaCena);
        values.put("totalPoreskaOsnovica", totalPoreskaOsnovica);
        values.put("totalProdajnaVrednost", totalProdajnaVrednost);
        return values;
    }
}
