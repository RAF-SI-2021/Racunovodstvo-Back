package raf.si.racunovodstvo.nabavka.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;
import raf.si.racunovodstvo.nabavka.responses.ArtikalResponse;

import java.util.Optional;

public interface IArtikalService extends IService<Artikal, Long> {

    Page<ArtikalResponse> findAll(Pageable pageable);

    Page<ArtikalResponse> findAllKalkulacijaArtikli(Pageable pageable);

    Page<ArtikalResponse> findAllByIdKalkulacijaKonverzija(Pageable pageable, Long idKalkulacijaKonverzija);

    ArtikalResponse save(ArtikalRequest artikalRequest);

    ArtikalResponse update(ArtikalRequest artikalRequest);

    Page<ArtikalResponse> findAll(Specification<Artikal> spec, Pageable pageSort);

    Optional<ArtikalResponse> findArtikalById(Long id);

    Page<ArtikalResponse> findAllKalkulacijaArtikli(Specification<Artikal> spec, Pageable pageSort);
}
