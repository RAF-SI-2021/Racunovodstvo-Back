package raf.si.racunovodstvo.preduzece.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.preduzece.model.*;
import raf.si.racunovodstvo.preduzece.model.enums.PolZaposlenog;
import raf.si.racunovodstvo.preduzece.model.enums.RadnaPozicija;
import raf.si.racunovodstvo.preduzece.model.enums.StatusZaposlenog;
import raf.si.racunovodstvo.preduzece.repositories.*;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaposleniRequest;
import raf.si.racunovodstvo.preduzece.services.impl.ObracunZaposleniService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(BootstrapData.class);
    private final PreduzeceRepository preduzeceRepository;
    private final ZaposleniRepository zaposleniRepository;
    private final StazRepository stazRepository;
    private final PlataRepository plataRepository;
    private final KoeficijentRepository koeficijentRepository;
    private final ObracunZaposleniService obracunZaposleniService;

    public BootstrapData(PreduzeceRepository preduzeceRepository,
                         ZaposleniRepository zaposleniRepository,
                         StazRepository stazRepository,
                         PlataRepository plataRepository,
                         KoeficijentRepository koeficijentRepository, ObracunZaposleniService obracunZaposleniService) {
        this.preduzeceRepository = preduzeceRepository;
        this.zaposleniRepository = zaposleniRepository;
        this.stazRepository = stazRepository;
        this.plataRepository = plataRepository;
        this.koeficijentRepository = koeficijentRepository;
        this.obracunZaposleniService = obracunZaposleniService;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Loading Data...");

        Preduzece p1 = new Preduzece();
        p1.setNaziv("Preduzece A");
        p1.setPib("111222333");
        p1.setAdresa("test adresa");
        p1.setGrad("Beograd");
        p1.setIsActive(true);

        Preduzece p2 = new Preduzece();
        p2.setNaziv("Preduzece B");
        p2.setPib("333222111");
        p2.setAdresa("test adresa");
        p2.setGrad("Novi Sad");
        p2.setIsActive(true);

        this.preduzeceRepository.save(p1);
        this.preduzeceRepository.save(p2);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Zaposleni zaposleni = new Zaposleni();
        zaposleni.setIme("Marko");
        zaposleni.setPrezime("Markovic");
        zaposleni.setPreduzece(p1);
        zaposleni.setPocetakRadnogOdnosa(new Date());
        zaposleni.setJmbg("1234567890123");
        zaposleni.setGrad("Beograd");
        zaposleni.setAdresa("Dorcol");
        zaposleni.setPol(PolZaposlenog.MUSKO);
        zaposleni.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        zaposleni.setDatumRodjenja(new Date());
        zaposleni.setRadnaPozicija(RadnaPozicija.DIREKTOR);
        zaposleni.setPreduzece(p1);
        zaposleniRepository.save(zaposleni);

        Zaposleni zaposleni2 = new Zaposleni();
        zaposleni2.setPreduzece(p1);
        zaposleni2.setIme("Petar");
        zaposleni2.setPrezime("Peric");
        zaposleni2.setPocetakRadnogOdnosa(new Date());
        zaposleni2.setJmbg("3210987654321");
        zaposleni2.setGrad("Novi Sad");
        zaposleni2.setAdresa("Centar");
        zaposleni2.setPol(PolZaposlenog.MUSKO);
        zaposleni2.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        zaposleni2.setDatumRodjenja(new Date());
        zaposleni2.setRadnaPozicija(RadnaPozicija.MENADZER);
        zaposleni2.setPreduzece(p1);
        zaposleniRepository.save(zaposleni2);

        Staz staz = new Staz();
        staz.setPocetakRada(simpleDateFormat.parse("08-08-2010"));
        staz.setKrajRada(null);
        staz.setZaposleni(zaposleni);
        stazRepository.save(staz);

        Staz staz2 = new Staz();
        staz2.setPocetakRada(new Date());
        staz2.setKrajRada(null);
        staz2.setZaposleni(zaposleni2);
        stazRepository.save(staz2);

        Plata plata = new Plata();
        plata.setNetoPlata(100000.0);
        plata.setZaposleni(zaposleni);
        plata.setDatumOd(new Date());
        plata.setDatumDo(null);
        plata.setBrutoPlata(150000.0);
        plata.setDoprinos1(1.2);
        plata.setDoprinos2(1.2);
        plata.setPorez(0.2);


        plataRepository.save(plata);

        Plata plata2 = new Plata();
        plata2.setNetoPlata(70000.0);
        plata2.setZaposleni(zaposleni2);
        plata2.setDatumOd(new Date());
        plata2.setDatumDo(null);
        plata.setBrutoPlata(150000.0);
        plata.setDoprinos1(1.2);
        plata.setDoprinos2(1.2);
        plata.setPorez(0.2);

        plataRepository.save(plata2);

        Koeficijent koeficijent = new Koeficijent();
        koeficijent.setKoeficijentPoreza(1d);
        koeficijent.setNezaposlenost1(2d);
        koeficijent.setNezaposlenost2(10d);
        koeficijent.setPenzionoOsiguranje1(5d);
        koeficijent.setPenzionoOsiguranje2(50d);
        koeficijent.setNajnizaOsnovica(1d);
        koeficijent.setNajvisaOsnovica(1d);
        koeficijent.setZdravstvenoOsiguranje1(5d);
        koeficijent.setZdravstvenoOsiguranje2(5d);
        koeficijent.setPoreskoOslobadjanje(23.4);
        koeficijentRepository.save(koeficijent);

       obracunZaposleniService.makeObracun(new Date(), 1);

        log.info("Data loaded!");
    }
}
