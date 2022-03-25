package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.DnevnikKnjizenja;
import rs.raf.demo.model.Faktura;

import java.util.List;
import java.util.Optional;

@Repository
public interface DnevnikKnjizenjaRepository extends JpaRepository<DnevnikKnjizenja, Long> {


    public List<DnevnikKnjizenja> serach(int brojNaloga, Faktura brojDokumenta);

    public Optional<DnevnikKnjizenja> findByDnevnikKnjizenjaId(Long dnevnikKnjizenjaId);

}
