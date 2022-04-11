package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;

import java.util.Date;
import java.util.List;

@Repository
public interface KontnaGrupaRepository extends JpaRepository<KontnaGrupa, Long> {

    @Query(
        "select new raf.si.racunovodstvo.knjizenje.responses.BilansResponse(sum(k.duguje), sum(k.potrazuje), count(k), kg.brojKonta, kg.nazivKonta) "
            + "from KontnaGrupa kg join kg.konto k "
            + "where :brojKontaOd <= kg.brojKonta and "
            + "(:brojKontaDo >= kg.brojKonta or kg.brojKonta like :brojKontaDo%) and "
            + "k.knjizenje.datumKnjizenja between :datumOd and :datumDo "
            + "group by kg.brojKonta, kg.nazivKonta")
    List<BilansResponse> findAllForBilans(String brojKontaOd, String brojKontaDo, Date datumOd, Date datumDo);

    @Query(value =
        "select new raf.si.racunovodstvo.knjizenje.responses.BilansResponse(sum(k.duguje), sum(k.potrazuje), count(k), kg.brojKonta , kg.nazivKonta) "
            + "from KontnaGrupa kg join kg.konto k where "
            + "substring(kg.brojKonta, 1, 1) in :startsWith "
            + "and k.knjizenje.datumKnjizenja between :datumOd and :datumDo "
            + "group by kg.brojKonta, kg.nazivKonta")
    List<BilansResponse> findAllStartingWith(List<String> startsWith, Date datumOd, Date datumDo);
}
