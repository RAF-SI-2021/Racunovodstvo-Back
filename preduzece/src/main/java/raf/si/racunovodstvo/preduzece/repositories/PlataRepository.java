package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.preduzece.model.Plata;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlataRepository extends JpaRepository<Plata, Long> {

    public List<Plata> findByZaposleniZaposleniId(Long zaposleniId);

    public Optional<Plata> findByPlataId(Long plataId);

    public List<Plata> findAll(Specification<Plata> spec);

    @Query("select p from Plata p where p.zaposleni = :zaposleni and (:datum >= p.datumOd and (p.datumDo is null or :datum <= p.datumDo))")
    public Plata findPlatabyDatumAndZaposleni(Date datum, Zaposleni zaposleni);

    @Query("select p from Plata p where p.zaposleni.statusZaposlenog = :statusZaposlenog and (:datum >= p.datumOd and (p.datumDo is null or :datum <= p.datumDo))")
    List<Plata> findPlataByDatumAndStatusZaposlenog(Date datum, StatusZaposlenog statusZaposlenog);
}
