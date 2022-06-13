package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.requests.SifraTransakcijeRequest;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;

public interface ISifraTransakcijeService extends IService<SifraTransakcije, Long>{

    Page<SifraTransakcijeResponse> findAll(Pageable pageable, String token);

    Page<SifraTransakcijeResponse> search(Specification<SifraTransakcije> specification, Pageable pageable, String token);

    SifraTransakcijeResponse save(SifraTransakcijeRequest sifraTransakcijeRequest);

    SifraTransakcijeResponse update(SifraTransakcijeRequest sifraTransakcijeRequest);
}
