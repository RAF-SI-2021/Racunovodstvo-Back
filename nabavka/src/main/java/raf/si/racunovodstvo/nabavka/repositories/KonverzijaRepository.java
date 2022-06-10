package raf.si.racunovodstvo.nabavka.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.nabavka.model.Konverzija;



import java.util.List;

@Repository
public interface KonverzijaRepository extends JpaRepository<Konverzija, Long> {

    public List<Konverzija> findAll();

    Page<Konverzija> findAll(Specification<Konverzija> spec, Pageable pageSort);
}
