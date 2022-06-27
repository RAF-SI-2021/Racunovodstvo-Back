package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.SifraTransakcijeConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.SifraTransakcijeReverseConverter;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.SifraTransakcijeRepository;
import raf.si.racunovodstvo.knjizenje.requests.SifraTransakcijeRequest;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.ISifraTransakcijeService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class SifraTransakcijeService implements ISifraTransakcijeService {

    private final SifraTransakcijeRepository sifraTransakcijeRepository;
    private final IConverter<SifraTransakcije, SifraTransakcijeResponse> sifraTransakcijeReverseConverter;
    private final IConverter<SifraTransakcijeRequest, SifraTransakcije> sifraTransakcijeConverter;

    @Autowired
    public SifraTransakcijeService(SifraTransakcijeRepository sifraTransakcijeRepository,
                                   SifraTransakcijeReverseConverter sifraTransakcijeReverseConverter,
                                   SifraTransakcijeConverter sifraTransakcijeConverter) {
        this.sifraTransakcijeRepository = sifraTransakcijeRepository;
        this.sifraTransakcijeReverseConverter = sifraTransakcijeReverseConverter;
        this.sifraTransakcijeConverter = sifraTransakcijeConverter;
    }

    @Override
    public Page<SifraTransakcijeResponse> findAll(Pageable pageable, String token) {
        return sifraTransakcijeRepository.findAll(pageable).map(sifraTransakcije -> {
            SifraTransakcijeResponse sifraTransakcijeResponse = sifraTransakcijeReverseConverter.convert(sifraTransakcije);
            return sifraTransakcijeResponse;
        });
    }

    @Override
    public Page<SifraTransakcijeResponse> search(Specification<SifraTransakcije> specification, Pageable pageable, String token) {
        return sifraTransakcijeRepository.findAll(specification, pageable).map(sifraTransakcije -> {
            SifraTransakcijeResponse sifraTransakcijeResponse = sifraTransakcijeReverseConverter.convert(sifraTransakcije);
            return sifraTransakcijeResponse;
        });
    }

    @Override
    public SifraTransakcijeResponse save(SifraTransakcijeRequest sifraTransakcijeRequest) {
        SifraTransakcije converted = sifraTransakcijeConverter.convert(sifraTransakcijeRequest);
        return sifraTransakcijeReverseConverter.convert(sifraTransakcijeRepository.save(converted));
    }
    
    @Override
    public SifraTransakcijeResponse update(SifraTransakcijeRequest sifraTransakcijeRequest) {
        Optional<SifraTransakcije> optionalSifraTransakcije = sifraTransakcijeRepository.findById(sifraTransakcijeRequest.getSifraTransakcijeId());
        if (optionalSifraTransakcije.isEmpty()) {
            throw new EntityNotFoundException();
        }
        SifraTransakcije converted = sifraTransakcijeConverter.convert(sifraTransakcijeRequest);
        return sifraTransakcijeReverseConverter.convert(sifraTransakcijeRepository.save(converted));
    }

    @Override
    public <S extends SifraTransakcije> S save(S var1) {
        return sifraTransakcijeRepository.save(var1);
    }

    @Override
    public Optional<SifraTransakcije> findById(Long var1) {
        return sifraTransakcijeRepository.findById(var1);
    }

    @Override
    public List<SifraTransakcije> findAll() {
        return sifraTransakcijeRepository.findAll();
    }

    @Override
    public void deleteById(Long var1) {
        Optional<SifraTransakcije> optionalSifraTransakcije = sifraTransakcijeRepository.findById(var1);
        if (optionalSifraTransakcije.isEmpty()) {
            throw new EntityNotFoundException();
        }
        sifraTransakcijeRepository.deleteById(var1);
    }
}