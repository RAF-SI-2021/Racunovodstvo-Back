package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.constants.RedisConstants;
import raf.si.racunovodstvo.knjizenje.converter.KontoConverter;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.repositories.KontoRepository;
import raf.si.racunovodstvo.knjizenje.responses.GlavnaKnjigaResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IService;

import java.util.List;
import java.util.Optional;

@Service
public class KontoService implements IService<Konto, Long> {

    @Lazy
    @Autowired
    private KontoConverter kontoConverter;

    private final KontoRepository kontoRepository;

    @Autowired
    public KontoService(KontoRepository kontoRepository) {
        this.kontoRepository = kontoRepository;
    }

    @Override
    @CachePut(value = RedisConstants.KONTO_CACHE, key = "#konto.kontoId")
    public Konto save(Konto konto) {
        return kontoRepository.save(konto);
    }

    @Override
    @Cacheable(value = RedisConstants.KONTO_CACHE, key = "#id")
    public Optional<Konto> findById(Long id) {
        return kontoRepository.findById(id);
    }

    @Override
    public List<Konto> findAll() {
        return kontoRepository.findAll();
    }

    @Override
    @CacheEvict(value = RedisConstants.KONTO_CACHE, key = "#id")
    public void deleteById(Long id) {
        kontoRepository.deleteById(id);
    }

    public List<Konto> findByKontnaGrupa(String kontnaGrupa) {
        return this.kontoRepository.findKontoByKontnaGrupaBrojKonta(kontnaGrupa);
    }

    public List<Konto> findAll(Specification<Konto> spec) {
        return this.kontoRepository.findAll(spec);
    }

    public Page<GlavnaKnjigaResponse> findAllGlavnaKnjigaResponseWithFilter(Specification<Konto> spec, Pageable pageSort) {
        return this.kontoConverter.convert(this.kontoRepository.findAll(spec, pageSort).getContent());
    }

    public Page<GlavnaKnjigaResponse> findAllGlavnaKnjigaResponse() {
        return this.kontoConverter.convert(this.kontoRepository.findAll());
    }
}
