package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.SifraTransakcijeRepository;
import raf.si.racunovodstvo.knjizenje.services.impl.IService;

import java.util.List;
import java.util.Optional;

@Service
public class SifraTransakcijeService implements IService<SifraTransakcije, Long> {

    private final SifraTransakcijeRepository sifraTransakcijeRepository;

    @Autowired
    public SifraTransakcijeService(SifraTransakcijeRepository sifraTransakcijeRepository) {
        this.sifraTransakcijeRepository = sifraTransakcijeRepository;
    }

    @Override
    public SifraTransakcije save(SifraTransakcije sifraTransakcije) {
        return sifraTransakcijeRepository.save(sifraTransakcije);
    }

    @Override
    public Optional<SifraTransakcije> findById(Long id) {
        return sifraTransakcijeRepository.findById(id);
    }

    @Override
    public List<SifraTransakcije> findAll() {
        return sifraTransakcijeRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        sifraTransakcijeRepository.deleteById(id);
    }
}
