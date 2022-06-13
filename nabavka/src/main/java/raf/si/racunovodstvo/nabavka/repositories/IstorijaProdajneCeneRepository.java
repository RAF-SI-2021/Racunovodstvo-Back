package raf.si.racunovodstvo.nabavka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.model.IstorijaProdajneCene;

@Repository
public interface IstorijaProdajneCeneRepository extends JpaRepository<IstorijaProdajneCene, Long> {

}
