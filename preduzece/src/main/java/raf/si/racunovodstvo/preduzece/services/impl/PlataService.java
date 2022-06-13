package raf.si.racunovodstvo.preduzece.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.constants.RedisConstants;
import raf.si.racunovodstvo.preduzece.model.Plata;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.repositories.PlataRepository;
import raf.si.racunovodstvo.preduzece.requests.PlataRequest;
import raf.si.racunovodstvo.preduzece.responses.PlataResponse;
import raf.si.racunovodstvo.preduzece.services.IPlataService;
import raf.si.racunovodstvo.preduzece.services.IZaposleniService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

@Service
public class PlataService implements IPlataService {

    private final PlataRepository plataRepository;
    private final IZaposleniService zaposleniService;
    private final KoeficijentService koeficijentService;
    private final ModelMapper modelMapper;

    public PlataService(PlataRepository plataRepository,
                        KoeficijentService koeficijentService,
                        IZaposleniService zaposleniService,
                        ModelMapper modelMapper) {
        this.plataRepository = plataRepository;
        this.koeficijentService = koeficijentService;
        this.zaposleniService = zaposleniService;
        this.modelMapper = modelMapper;
    }

    @Override
    public <S extends Plata> S save(S var1) {
        return this.plataRepository.save(var1);
    }

    public Plata save(PlataRequest plataRequest) {
        Optional<Zaposleni> optionalZaposleni = zaposleniService.findById(plataRequest.getZaposleniId());

        if (optionalZaposleni.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Zaposleni zaposleni = optionalZaposleni.get();
        updateDatumDoNaStarojPlati(zaposleni);
        Plata plata = new Plata();
        plata.setNetoPlata(plataRequest.getNetoPlata());
        plata.setDatumOd(plataRequest.getDatum());
        plata.setZaposleni(zaposleni);
        plata.izracunajDoprinose(this.koeficijentService.getCurrentKoeficijent());
        return this.plataRepository.save(plata);
    }

    private void updateDatumDoNaStarojPlati(Zaposleni zaposleni) {
        List<Plata> plate = plataRepository.findAll()
                                           .stream()
                                           .filter(plata -> plata.getZaposleni().getZaposleniId().equals(zaposleni.getZaposleniId())
                                               && plata.getDatumDo() == null)
                                           .collect(Collectors.toList());

        if (plate.size() == 1) {
            Plata plata = plate.get(0);
            plata.setDatumDo(new Date());
            plataRepository.save(plata);
        }

    }

    @Override
    public Optional<Plata> findById(Long var1) {
        return this.plataRepository.findByPlataId(var1);
    }

    @Override
    public List<Plata> findAll() {
        return this.plataRepository.findAll();
    }

    public List<Plata> findAll(Specification<Plata> spec) {
        return this.plataRepository.findAll(spec);
    }

    @Override
    @CacheEvict(value = RedisConstants.PLATA_CACHE, key = "#var1")
    public void deleteById(Long var1) {
        this.plataRepository.deleteById(var1);
    }

    public List<Plata> findByZaposleniZaposleniId(Long zaposleniId) {
        return this.plataRepository.findByZaposleniZaposleniId(zaposleniId);
    }

    public Plata findPlatabyDatumAndZaposleni(Date datumObracuna, Zaposleni zaposleni) {
        return plataRepository.findPlatabyDatumAndZaposleni(datumObracuna, zaposleni);
    }

    @Override
    @Cacheable(value = RedisConstants.PLATA_CACHE, key = "#id")
    public Optional<PlataResponse> customFindById(Long id) {
        Optional<Plata> optionalPlata = findById(id);
        return optionalPlata.map(plata -> modelMapper.map(plata, PlataResponse.class));
    }

    @Override
    @CachePut(value = RedisConstants.PLATA_CACHE, key = "#result.plataId")
    public PlataResponse customSave(PlataRequest plataRequest) {
        return modelMapper.map(save(plataRequest), PlataResponse.class);
    }

    @Override
    public List<PlataResponse> customFindAll() {
        return findAll().stream().map(plata -> modelMapper.map(plata, PlataResponse.class)).collect(Collectors.toList());
    }

    @Override
    public List<PlataResponse> customFindAll(Specification<Plata> spec) {
        return findAll(spec).stream().map(plata -> modelMapper.map(plata, PlataResponse.class)).collect(Collectors.toList());
    }
}
