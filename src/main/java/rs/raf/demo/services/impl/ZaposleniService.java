package rs.raf.demo.services.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.raf.demo.exceptions.OperationNotSupportedException;
import rs.raf.demo.model.Staz;
import rs.raf.demo.model.Zaposleni;
import rs.raf.demo.model.enums.StatusZaposlenog;
import rs.raf.demo.repositories.ZaposleniRepository;
import rs.raf.demo.services.IZaposleniService;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ZaposleniService implements IZaposleniService{


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
            for(Staz staz : currZaposleni.get().getStaz()){
                if(staz.getKrajRada() == null){
                    staz.setKrajRada(new Date());
                    stazService.save(staz);
                }
            }

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