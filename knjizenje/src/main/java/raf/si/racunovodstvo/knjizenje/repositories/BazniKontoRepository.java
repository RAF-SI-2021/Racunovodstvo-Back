package raf.si.racunovodstvo.knjizenje.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;

import java.util.List;

@Repository
public interface BazniKontoRepository extends JpaRepository<BazniKonto, Long> {

    public List<BazniKonto> findAll();
}
