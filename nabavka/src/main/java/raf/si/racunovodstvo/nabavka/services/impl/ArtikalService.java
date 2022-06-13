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
import raf.si.racunovodstvo.nabavka.converters.impl.ArtikalConverter;
import raf.si.racunovodstvo.nabavka.converters.impl.ArtikalReverseConverter;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.model.IstorijaProdajneCene;
import raf.si.racunovodstvo.nabavka.model.KalkulacijaArtikal;
import raf.si.racunovodstvo.nabavka.repositories.ArtikalRepository;
import raf.si.racunovodstvo.nabavka.repositories.IstorijaProdajneCeneRepository;
import raf.si.racunovodstvo.nabavka.repositories.KalkulacijaArtikalRepository;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;
import raf.si.racunovodstvo.nabavka.responses.ArtikalResponse;
import raf.si.racunovodstvo.nabavka.services.IArtikalService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class ArtikalService implements IArtikalService {

    private final ArtikalRepository artikalRepository;
    private final KalkulacijaArtikalRepository kalkulacijaArtikalRepository;
    private final IstorijaProdajneCeneRepository istorijaProdajneCeneRepository;
    private final IConverter<Artikal, ArtikalResponse> artikalReverseConverter;
    private final IConverter<ArtikalRequest, Artikal> artikalConverter;

    public ArtikalService(ArtikalRepository artikalRepository,
                          KalkulacijaArtikalRepository kalkulacijaArtikalRepository,
                          IstorijaProdajneCeneRepository istorijaProdajneCeneRepository,
                          ArtikalReverseConverter artikalReverseConverter,
                          ArtikalConverter artikalConverter) {
        this.artikalRepository = artikalRepository;
        this.kalkulacijaArtikalRepository = kalkulacijaArtikalRepository;
        this.istorijaProdajneCeneRepository = istorijaProdajneCeneRepository;
        this.artikalReverseConverter = artikalReverseConverter;
        this.artikalConverter = artikalConverter;
    }

    @Override
    public Page<ArtikalResponse> findAll(Pageable pageable) {
        return artikalRepository.findAll(pageable).map(artikalReverseConverter::convert);
    }

    @Override
    public Page<ArtikalResponse> findAllKalkulacijaArtikli(Pageable pageable) {
        return kalkulacijaArtikalRepository.findAll(pageable).map(artikalReverseConverter::convert);
    }

    @Override
    public Page<ArtikalResponse> findAllByIdKalkulacijaKonverzija(Pageable pageable, Long idKalkulacijaKonverzija) {
        return artikalRepository.findAllByBaznaKonverzijaKalkulacijaId(pageable, idKalkulacijaKonverzija)
                                .map(artikalReverseConverter::convert);
    }

    @Override
    @CachePut(value = RedisConstants.ARTIKAL_CACHE, key = "#result.artikalId")
    public ArtikalResponse save(ArtikalRequest artikalRequest) {
        Artikal converted = artikalConverter.convert(artikalRequest);
        Artikal saved = artikalRepository.save(converted);
        return artikalReverseConverter.convert(saved);
    }

    @Override
    @CachePut(value = RedisConstants.ARTIKAL_CACHE, key = "#result.artikalId")
    public ArtikalResponse update(ArtikalRequest artikalRequest) {
        System.out.println(artikalRequest.getArtikalId());
        Optional<ArtikalResponse> optionalArtikal = findArtikalById(artikalRequest.getArtikalId());
        if (optionalArtikal.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Artikal converted = artikalConverter.convert(artikalRequest);
        if (artikalRequest.isAktivanZaProdaju()) {
            handleIstorijaProdaje((KalkulacijaArtikal) converted);
        }
        return artikalReverseConverter.convert(artikalRepository.save(converted));
    }

    @Override
    public Page<ArtikalResponse> findAll(Specification<Artikal> spec, Pageable pageSort) {
        return artikalRepository.findAll(spec, pageSort).map(artikalReverseConverter::convert);
    }

    @Override
    @Cacheable(value = RedisConstants.ARTIKAL_CACHE, key = "#id")
    public Optional<ArtikalResponse> findArtikalById(Long id) {
        return findById(id).map(artikalReverseConverter::convert);
    }

    @Override
    public Page<ArtikalResponse> findAllKalkulacijaArtikli(Specification<Artikal> spec, Pageable pageSort) {
        return kalkulacijaArtikalRepository.findAll(spec, pageSort).map(artikalReverseConverter::convert);
    }

    private void handleIstorijaProdaje(KalkulacijaArtikal artikal) {
        if (!isKalkulacijaArtikalSaved(artikal)) {
            return;
        }

        KalkulacijaArtikal savedKalkulacijaArtikal = (KalkulacijaArtikal)findById(artikal.getArtikalId()).get();
        List<IstorijaProdajneCene> istorijaProdajneCeneList = savedKalkulacijaArtikal.getIstorijaProdajneCene();
        if (isProdajnaCenaUpdated(artikal)) {
            IstorijaProdajneCene istorijaProdajneCene = new IstorijaProdajneCene();
            istorijaProdajneCene.setProdajnaCena(savedKalkulacijaArtikal.getProdajnaCena());
            istorijaProdajneCene.setTimestamp(new Date());
            istorijaProdajneCene = istorijaProdajneCeneRepository.save(istorijaProdajneCene);
            istorijaProdajneCeneList.add(istorijaProdajneCene);
        }
        artikal.setIstorijaProdajneCene(new ArrayList<>(istorijaProdajneCeneList));
    }

    private boolean isProdajnaCenaUpdated(KalkulacijaArtikal artikal) {
        ArtikalResponse savedArtikal = findArtikalById(artikal.getArtikalId()).get();
        return !Objects.equals(savedArtikal.getProdajnaCena(), artikal.getProdajnaCena());
    }

    private boolean isKalkulacijaArtikalSaved(KalkulacijaArtikal artikal) {
        if (artikal.getArtikalId() == null) {
            return false;
        }
        Optional<Artikal> optionalArtikal = findById(artikal.getArtikalId());
        if (optionalArtikal.isEmpty()) {
            return false;
        }
        return optionalArtikal.get() instanceof KalkulacijaArtikal;
    }

    @Override
    public <S extends Artikal> S save(S var1) {
        return artikalRepository.save(var1);
    }

    @Override
    public Optional<Artikal> findById(Long var1) {
        return artikalRepository.findById(var1);
    }

    @Override
    public List<Artikal> findAll() {
        return artikalRepository.findAll();
    }

    @Override
    @CacheEvict(value = RedisConstants.ARTIKAL_CACHE, key = "#var1")
    public void deleteById(Long var1) {
        Optional<Artikal> optionalArtikal = artikalRepository.findById(var1);
        if (optionalArtikal.isEmpty()) {
            throw new EntityNotFoundException();
        }
        artikalRepository.deleteById(var1);
    }
}
