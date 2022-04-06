package rs.raf.demo.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import rs.raf.demo.model.Knjizenje;
import rs.raf.demo.responses.KnjizenjeResponse;

import java.util.List;


public interface IKnjizenjeService extends IService<Knjizenje, Long> {

    Page<KnjizenjeResponse> findAll(Specification<Knjizenje> spec, Pageable pageSort);

    Double getSumaPotrazujeZaKnjizenje(Long id);

    Double getSumaDugujeZaKnjizenje(Long id);

    Double getSaldoZaKnjizenje(Long id);

    List<KnjizenjeResponse> findAllKnjizenjeResponse();
}