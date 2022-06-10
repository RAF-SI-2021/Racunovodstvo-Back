package raf.si.racunovodstvo.nabavka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;

@Repository
public interface TroskoviNabavkeRepository extends JpaRepository<TroskoviNabavke, Long> {
}
