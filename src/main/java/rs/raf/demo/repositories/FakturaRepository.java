package rs.raf.demo.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Faktura;
import rs.raf.demo.model.TipFakture;

import java.util.List;
import java.util.Optional;

@Repository
public interface FakturaRepository extends JpaRepository<Faktura, Long> {

    public List<Faktura> findAll();

    List<Faktura> findAll(Specification<Faktura> spec);

    public Optional<Faktura> findByFakturaId(Long fakturaId);

    @Query("select f.prodajnaVrednost from Faktura f where f.tipFakture = :tipFakture")
    List<Double> findProdajnaVrednostForTipFakture(TipFakture tipFakture);

    @Query("select f.rabat from Faktura f where f.tipFakture = :tipFakture")
    List<Double> findRabatForTipFakture(TipFakture tipFakture);

    @Query("select f.porez from Faktura f where f.tipFakture = :tipFakture")
    List<Double> findPorezForTipFakture(TipFakture tipFakture);

    @Query("select f.naplata from Faktura f where f.tipFakture = :tipFakture")
    List<Double> findNaplataForTipFakture(TipFakture tipFakture);
}
