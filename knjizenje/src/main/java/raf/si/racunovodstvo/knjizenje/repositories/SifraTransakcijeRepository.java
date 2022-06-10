package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;

@Repository
public interface SifraTransakcijeRepository extends JpaRepository<SifraTransakcije, Long> {

    Page<SifraTransakcije> findAll(Specification<SifraTransakcije> specification, Pageable pageable);

}