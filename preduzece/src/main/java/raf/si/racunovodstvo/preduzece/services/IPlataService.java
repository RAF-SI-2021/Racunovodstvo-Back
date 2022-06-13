package raf.si.racunovodstvo.preduzece.services;

import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.model.Plata;
import raf.si.racunovodstvo.preduzece.requests.PlataRequest;
import raf.si.racunovodstvo.preduzece.responses.PlataResponse;

import java.util.List;
import java.util.Optional;

public interface IPlataService extends IService<Plata, Long> {

    Optional<PlataResponse> customFindById(Long id);

    PlataResponse customSave(PlataRequest plataRequest);

    List<PlataResponse> customFindAll(Specification<Plata> specification);

    List<PlataResponse> customFindAll();
}
