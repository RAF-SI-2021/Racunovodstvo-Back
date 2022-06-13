package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransakcijaRepository extends JpaRepository<Transakcija, Long> {

    Page<Transakcija> findAll(Specification<Transakcija> specification, Pageable pageable);
    Optional<Transakcija> findByDokumentId(Long dokumentId);

    List<Transakcija> findAllByPreduzeceIdAndDatumTransakcijeBetween(long preduceId, Date dateFrom, Date dateTo);

    List<Transakcija> findAllByPreduzeceId(long preduceId);
}
