package rs.raf.demo.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.raf.demo.model.KontnaGrupa;

public interface IKontnaGrupaService extends IService<KontnaGrupa, String> {
    Page<KontnaGrupa> findAll(Pageable sort);
    KontnaGrupa update(String id);
    KontnaGrupa findKontnaGrupaById(String id);
}
