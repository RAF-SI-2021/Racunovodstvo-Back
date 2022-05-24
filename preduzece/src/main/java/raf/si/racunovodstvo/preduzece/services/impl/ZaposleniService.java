package raf.si.racunovodstvo.preduzece.services.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.preduzece.model.Staz;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;
import raf.si.racunovodstvo.preduzece.repositories.ZaposleniRepository;
import raf.si.racunovodstvo.preduzece.services.IZaposleniService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

@Service
public class ZaposleniService implements IZaposleniService {


    private final ZaposleniRepository zaposleniRepository;
    private final StazService stazService;


    public ZaposleniService(ZaposleniRepository zaposleniRepository, StazService stazService) {
        this.zaposleniRepository = zaposleniRepository;
        this.stazService = stazService;
    }

    @Override
    public <S extends Zaposleni> S save(S zaposleni) {
        Staz newStaz = new Staz();
        newStaz.setPocetakRada(new Date());
        newStaz.setKrajRada(null);
        stazService.save(newStaz);

        zaposleni.setStaz(List.of(newStaz));
        zaposleni.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);

        return zaposleniRepository.save(zaposleni);
    }

    @Override
    public Optional<Zaposleni> findById(Long id) {
        return zaposleniRepository.findById(id);
    }

    @Override
    public List<Zaposleni> findAll() {
        return zaposleniRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        zaposleniRepository.deleteById(id);
    }

    @Override
    public List<Zaposleni> findAll(Specification<Zaposleni> spec) {
        return zaposleniRepository.findAll(spec);
    }

    @Override
    public Zaposleni otkazZaposleni(Zaposleni zaposleni) {

        Optional<Zaposleni> currZaposleni = zaposleniRepository.findById(zaposleni.getZaposleniId());

        currZaposleni.get().setStatusZaposlenog(StatusZaposlenog.NEZAPOSLEN);

        List<Staz> staz = currZaposleni.get().getStaz()
                                       .stream()
                                       .filter(s -> s.getKrajRada() == null).collect(Collectors.toList());

        if (staz.size() != 1) {
            throw new OperationNotSupportedException("Zaposleni nije u radnom odnosu ili ima vise radnih mesta!");
        }

        staz.get(0).setKrajRada(new Date());
        stazService.save(staz.get(0));

        return zaposleniRepository.save(currZaposleni.get());
    }

    @Override
    public Zaposleni updateZaposleni(Zaposleni zaposleni) {

        Optional<Zaposleni> currZaposleni = zaposleniRepository.findById(zaposleni.getZaposleniId());

        if(!currZaposleni.isPresent()){
            throw new EntityNotFoundException();
        }

        if(currZaposleni.get().getStatusZaposlenog().equals(StatusZaposlenog.ZAPOSLEN) && zaposleni.getStatusZaposlenog().equals(StatusZaposlenog.NEZAPOSLEN)) {
            throw new OperationNotSupportedException("Nije dozvoljeno update statusa u nezaposlen na ovoj ruti.");
        }

        if(currZaposleni.get().getStatusZaposlenog().equals(StatusZaposlenog.NEZAPOSLEN) && zaposleni.getStatusZaposlenog().equals(StatusZaposlenog.ZAPOSLEN)) {
            Staz newStaz = new Staz();
            newStaz.setPocetakRada(new Date());
            newStaz.setKrajRada(null);
            stazService.save(newStaz);

            List<Staz> stazLista = zaposleniRepository.findById(zaposleni.getZaposleniId()).get().getStaz();
            stazLista.add(newStaz);
            zaposleni.setStaz(stazLista);

        }else{
            List<Staz> stazLista = zaposleniRepository.findById(zaposleni.getZaposleniId()).get().getStaz();
            zaposleni.setStaz(stazLista);
        }

        return zaposleniRepository.save(zaposleni);
    }
}