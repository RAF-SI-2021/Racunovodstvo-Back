package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.repositories.TransakcijaRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.ITransakcijaService;

import java.util.List;
import java.util.Optional;

@Service
public class TransakcijaService implements ITransakcijaService {

    private final TransakcijaRepository transakcijaRepository;

    @Autowired
    public TransakcijaService(TransakcijaRepository transakcijaRepository) {
        this.transakcijaRepository = transakcijaRepository;
    }

    @Override
    public Transakcija save(Transakcija transakcija){
        return transakcijaRepository.save(transakcija);
    }

    @Override
    public Optional<Transakcija> findById(Long id) {
        return transakcijaRepository.findByDokumentId(id);
    }

    @Override
    public List<Transakcija> findAll() {
        return transakcijaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        transakcijaRepository.deleteById(id);
    }

    @Override
    public List<Transakcija> findAll(Specification<Transakcija> spec) {
        return transakcijaRepository.findAll(spec);
    }

    @Override
    public Page<Transakcija> findAll(Pageable pageSort) {
        return transakcijaRepository.findAll(pageSort);
    }
}
