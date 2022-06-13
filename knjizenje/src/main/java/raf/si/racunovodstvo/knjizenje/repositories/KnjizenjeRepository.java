package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;

import java.util.Date;


@Repository
public interface KnjizenjeRepository extends JpaRepository<Knjizenje, Long> {

    Page<Knjizenje> findAll(Specification<Knjizenje> spec, Pageable pageSort);

    @Query("select k from knjizenje k"
        + " join k.dokument d"
        + " join k.konto ko"
        + " join ko.kontnaGrupa kg"
        + " where (kg.brojKonta like :brojKonta)"
        + " and (k.datumKnjizenja between :datumOd and :datumDo)"
        + " and (d.preduzeceId = :komitentId)"
        + " group by k")
    Page<Knjizenje> findAllByBrojKontaAndKomitentId(Pageable pageSort, String brojKonta, Long komitentId, Date datumOd, Date datumDo);
}
