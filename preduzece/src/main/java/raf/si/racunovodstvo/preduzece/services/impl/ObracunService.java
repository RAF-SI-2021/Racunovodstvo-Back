package raf.si.racunovodstvo.preduzece.services.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import raf.si.racunovodstvo.preduzece.feign.TransakcijeFeignClient;
import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.model.Transakcija;
import raf.si.racunovodstvo.preduzece.repositories.ObracunRepository;
import raf.si.racunovodstvo.preduzece.requests.ObracunTransakcijeRequest;
import raf.si.racunovodstvo.preduzece.services.IObracunService;

import java.util.*;

@Service
public class ObracunService implements IObracunService {

    private final ObracunRepository obracunRepository;
    private final TransakcijeFeignClient transakcijeFeignClient;

    public ObracunService(ObracunRepository obracunRepository, TransakcijeFeignClient transakcijeFeignClient) {
        this.obracunRepository = obracunRepository;
        this.transakcijeFeignClient = transakcijeFeignClient;
    }

    @Override
    public <S extends Obracun> S save(S var1) {
        return obracunRepository.save(var1);
    }

    @Override
    public Optional<Obracun> findById(Long var1) {
        return obracunRepository.findById(var1);
    }

    @Override
    public List<Obracun> findAll() {
        return obracunRepository.findAll();
    }

    @Override
    public void deleteById(Long var1) {
        obracunRepository.deleteById(var1);
    }

    private ObracunTransakcijeRequest getObracunTransakcijeRequest(ObracunZaposleni obracunZaposleni) {

        ObracunTransakcijeRequest obracunTransakcijeRequest = new ObracunTransakcijeRequest();
        obracunTransakcijeRequest.setDatum(new Date());
        obracunTransakcijeRequest.setIme(obracunZaposleni.getZaposleni().getIme());
        obracunTransakcijeRequest.setPrezime(obracunZaposleni.getZaposleni().getPrezime());
        obracunTransakcijeRequest.setIznos(obracunZaposleni.getUkupanTrosakZarade());
        obracunTransakcijeRequest.setPreduzeceId(obracunZaposleni.getZaposleni().getPreduzece().getPreduzeceId());
        obracunTransakcijeRequest.setSifraZaposlenog(obracunZaposleni.getZaposleni().getZaposleniId().toString());
        obracunTransakcijeRequest.setSifraTransakcijeId(obracunZaposleni.getObracun().getSifraTransakcije());

        return obracunTransakcijeRequest;

    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Obracun obradiObracun(Long obracunId, String token) {

        Optional<Obracun> optionalObracun = obracunRepository.findById(obracunId);

        if (optionalObracun.isEmpty()) {
            throw new RuntimeException(String.format("Obracun sa id-jem %s ne postoji", obracunId));
        }

        Obracun obracun = optionalObracun.get();

        if (obracun.isObradjen()) {
            throw new RuntimeException("Obracun je vec obradjen");
        }

        if(obracun.getSifraTransakcije() == 0){
            throw new RuntimeException("Nije postavljena sifra transakcije!");
        }

        List<ObracunZaposleni> obracunZaposleniList = obracun.getObracunZaposleniList();


        List<ObracunTransakcijeRequest> obracunTransakcijeRequestList = new ArrayList<>();

        Map<String, ObracunZaposleni> obracunZaposleniMap = new HashMap<>();
        for (ObracunZaposleni obracunZaposleni : obracunZaposleniList) {
            obracunZaposleniMap.put(obracunZaposleni.getZaposleni().getZaposleniId().toString(), obracunZaposleni);


            obracunTransakcijeRequestList.add(getObracunTransakcijeRequest(obracunZaposleni));
        }

        ResponseEntity<List<Transakcija>> response = transakcijeFeignClient.obracunZaradeTransakcije(obracunTransakcijeRequestList, token);

        if(response.getStatusCodeValue() != 200){
            throw new RuntimeException("Obrada nije uspela - dovlacenje transakcija nije uspelo");
        }

        List<Transakcija> transakcijaList = response.getBody();

        if (transakcijaList == null || transakcijaList.size() != obracun.getObracunZaposleniList().size()) {
            throw new RuntimeException("Obrada nije uspela");
        }
        for (Transakcija transakcija : transakcijaList) {
            try {
                String sifraZaposleni = transakcija.getBrojTransakcije().split("-")[1];
                ObracunZaposleni obracunZaposleni = obracunZaposleniMap.get(sifraZaposleni);

                if (obracunZaposleni == null) {
                    throw new RuntimeException("Obrada nije uspela");
                }
            } catch (Error e) {
                throw new RuntimeException("Obrada nije uspela");
            }
        }
        obracun.setObradjen(true);
        return save(obracun);
    }


    public void updateObracunZaradeNaziv(Long obracunZaradeId, String naziv) {
        Optional<Obracun> obracunOptional = obracunRepository.findById(obracunZaradeId);
        if (obracunOptional.isPresent()) {
            Obracun obracun = obracunOptional.get();
            obracun.setNaziv(naziv);
            save(obracun);
        }
    }
}
