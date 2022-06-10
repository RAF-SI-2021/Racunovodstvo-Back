package raf.si.racunovodstvo.nabavka.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;

@Repository
public interface KalkulacijaRepository extends JpaRepository<Kalkulacija, Long> {

    Page<Kalkulacija> findAll(Specification<Kalkulacija> spec, Pageable pageSort);

    Page<Kalkulacija> findAll(Pageable pageSort);
}
