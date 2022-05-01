package raf.si.racunovodstvo.nabavka.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;
import raf.si.racunovodstvo.nabavka.responses.ArtikalResponse;

public interface IArtikalService extends IService<Artikal, Long> {

    Page<ArtikalResponse> findAll(Pageable pageable);

    ArtikalResponse save(ArtikalRequest artikalRequest);

    ArtikalResponse update(ArtikalRequest artikalRequest);
}
