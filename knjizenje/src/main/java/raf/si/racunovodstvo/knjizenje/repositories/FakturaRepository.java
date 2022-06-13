package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;

import java.util.List;
import java.util.Optional;

@Repository
public interface FakturaRepository extends JpaRepository<Faktura, Long> {

    List<Faktura> findAll();

    List<Faktura> findAll(Specification<Faktura> spec);

    Optional<Faktura> findByDokumentId(Long dokumentId);

    @Query("select f.prodajnaVrednost from faktura f where f.tipFakture = :tipFakture")
    List<Double> findProdajnaVrednostForTipFakture(TipFakture tipFakture);

    @Query("select f.rabat from faktura f where f.tipFakture = :tipFakture")
    List<Double> findRabatForTipFakture(TipFakture tipFakture);

    @Query("select f.porez from faktura f where f.tipFakture = :tipFakture")
    List<Double> findPorezForTipFakture(TipFakture tipFakture);

    @Query("select f.naplata from faktura f where f.tipFakture = :tipFakture")
    List<Double> findNaplataForTipFakture(TipFakture tipFakture);
}
