package raf.si.racunovodstvo.knjizenje.services;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.TransakcijaRepository;
import raf.si.racunovodstvo.knjizenje.requests.ObracunTransakcijeRequest;
import raf.si.racunovodstvo.knjizenje.requests.TransakcijaRequest;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.ITransakcijaService;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.TransakcijaConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.TransakcijaReverseConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.EntityNotFoundException;

@Service
public class TransakcijaService implements ITransakcijaService {

    private final TransakcijaRepository transakcijaRepository;
    private final IConverter<Transakcija, TransakcijaResponse> transakcijaReverseConverter;
    private final IConverter<TransakcijaRequest, Transakcija> transakcijaConverter;
    private final PreduzeceFeignClient preduzeceFeignClient;

    public TransakcijaService(TransakcijaRepository transakcijaRepository,
                              TransakcijaReverseConverter transakcijaReverseConverter,
                              TransakcijaConverter transakcijaConverter,
                              PreduzeceFeignClient preduzeceFeignClient) {
        this.transakcijaRepository = transakcijaRepository;
        this.transakcijaReverseConverter = transakcijaReverseConverter;
        this.transakcijaConverter = transakcijaConverter;
        this.preduzeceFeignClient = preduzeceFeignClient;
    }

    @Override
    public Page<TransakcijaResponse> findAll(Pageable pageable, String token) {
        return transakcijaRepository.findAll(pageable).map(transakcija -> {
            TransakcijaResponse transakcijaResponse = transakcijaReverseConverter.convert(transakcija);
            transakcijaResponse.setKomitent(getKomitentForTransakcija(transakcija.getPreduzeceId(), token));
            return transakcijaResponse;
        });
    }

    @Override
    public Page<TransakcijaResponse> search(Specification<Transakcija> specification, Pageable pageable, String token) {
        return transakcijaRepository.findAll(specification, pageable).map(transakcija -> {
            TransakcijaResponse transakcijaResponse = transakcijaReverseConverter.convert(transakcija);
            transakcijaResponse.setKomitent(getKomitentForTransakcija(transakcija.getPreduzeceId(), token));
            return transakcijaResponse;
        });
    }

    @Override
    public TransakcijaResponse save(TransakcijaRequest transakcijaRequest) {
        Transakcija converted = transakcijaConverter.convert(transakcijaRequest);
        return transakcijaReverseConverter.convert(transakcijaRepository.save(converted));
    }

    @Override
    public TransakcijaResponse update(TransakcijaRequest transakcijaRequest) {
        Optional<Transakcija> optionalTransakcija = transakcijaRepository.findById(transakcijaRequest.getDokumentId());
        if (optionalTransakcija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Transakcija converted = transakcijaConverter.convert(transakcijaRequest);
        return transakcijaReverseConverter.convert(transakcijaRepository.save(converted));
    }

    @Override
    public List<Transakcija> obracunZaradeTransakcije(List<ObracunTransakcijeRequest> obracunTransakcijeRequests) {
        List<Transakcija> transakcijeList = new CopyOnWriteArrayList<>();
        for(ObracunTransakcijeRequest o : obracunTransakcijeRequests){
            Transakcija t = new Transakcija();
            t.setBrojTransakcije("OZ " + o.getDatum().getMonth()+ "/" + o.getDatum().getYear() + "-" + o.getSifraZaposlenog());
            t.setTipTransakcije(TipTransakcije.ISPLATA);
            t.setDatumTransakcije(o.getDatum());
            t.setIznos(o.getIznos());
            t.setPreduzeceId(o.getPreduzeceId());
            t.setSifraTransakcije(o.getSifraTransakcije());
            transakcijeList.add(t);
        }
        return transakcijaRepository.saveAll(transakcijeList);
    }

    @Override
    public <S extends Transakcija> S save(S var1) {
        return transakcijaRepository.save(var1);
    }

    @Override
    public Optional<Transakcija> findById(Long var1) {
        return transakcijaRepository.findById(var1);
    }

    @Override
    public List<Transakcija> findAll() {
        return transakcijaRepository.findAll();
    }

    @Override
    public void deleteById(Long var1) {
        Optional<Transakcija> optionalTransakcija = transakcijaRepository.findById(var1);
        if (optionalTransakcija.isEmpty()) {
            throw new EntityNotFoundException();
        }
        transakcijaRepository.deleteById(var1);
    }

    private String getKomitentForTransakcija(Long preduzeceId, String token) {
        if (preduzeceId != null) {
            Preduzece preduzece = preduzeceFeignClient.getPreduzeceById(preduzeceId, token).getBody();
            return preduzece == null ? null : preduzece.getNaziv();
        }
        return Strings.EMPTY;
    }
}
