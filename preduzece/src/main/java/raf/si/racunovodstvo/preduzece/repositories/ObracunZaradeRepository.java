package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import raf.si.racunovodstvo.preduzece.model.ObracunZarade;

import java.util.Date;
import java.util.List;

public interface ObracunZaradeRepository extends JpaRepository<ObracunZarade, Long> {
    @Query("select oz from ObracunZarade oz where oz.datumOd < :date and oz.datumDo > :date")
     List<ObracunZarade> findAllByDate(Date date);
}
