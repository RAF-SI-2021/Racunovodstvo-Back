package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransakcijaRepository extends JpaRepository<Transakcija, Long> {

    List<Transakcija> findAll(Specification<Transakcija> spec);
    Optional<Transakcija> findByDokumentId(Long dokumentId);
}
