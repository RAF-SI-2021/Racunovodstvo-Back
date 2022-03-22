package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Faktura;

import java.util.List;
import java.util.Optional;

@Repository
public interface FakturaRepository extends JpaRepository<Faktura, Long> {

    public List<Faktura> findAll();

    public Optional<Faktura> findByFakturaId(Long fakturaId);

}
