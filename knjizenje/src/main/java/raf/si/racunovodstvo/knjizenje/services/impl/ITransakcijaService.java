package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.requests.ObracunTransakcijeRequest;
import raf.si.racunovodstvo.knjizenje.requests.TransakcijaRequest;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;

import java.util.Date;
import java.util.List;

public interface ITransakcijaService extends IService<Transakcija, Long> {

    Page<TransakcijaResponse> findAll(Pageable pageable, String token);

    Page<TransakcijaResponse> search(Specification<Transakcija> specification, Pageable pageable, String token);

    TransakcijaResponse save(TransakcijaRequest transakcijaRequest);

    TransakcijaResponse update(TransakcijaRequest transakcijaRequest);

    List<Transakcija> obracunZaradeTransakcije(List<ObracunTransakcijeRequest> obracunTransakcijeRequests);

    List<TransakcijaResponse> findByPreduzeceId(long preduzeceId);

    List<TransakcijaResponse> findByPreduzeceIdAndDate(long preduzeceId, Date dateFrom, Date dateTo);

    TransakcijaResponse createFromMPFaktura(Faktura faktura);
}
