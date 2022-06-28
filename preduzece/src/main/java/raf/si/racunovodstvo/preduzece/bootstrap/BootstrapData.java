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
import java.util.ArrayList;
import java.util.Calendar;
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
    private final ObracunRepository obracunRepository;
    private final ObracunZaposleniRepository obracunZaposleniRepository;

    private final ObracunZaposleniService obracunZaposleniService;

    public BootstrapData(PreduzeceRepository preduzeceRepository,
                         ZaposleniRepository zaposleniRepository,
                         StazRepository stazRepository,
                         PlataRepository plataRepository,
                         KoeficijentRepository koeficijentRepository, ObracunRepository obracunRepository, ObracunZaposleniRepository obracunZaposleniRepository, ObracunZaposleniService obracunZaposleniService) {
        this.preduzeceRepository = preduzeceRepository;
        this.zaposleniRepository = zaposleniRepository;
        this.stazRepository = stazRepository;
        this.plataRepository = plataRepository;
        this.koeficijentRepository = koeficijentRepository;
        this.obracunRepository = obracunRepository;
        this.obracunZaposleniRepository = obracunZaposleniRepository;
        this.obracunZaposleniService = obracunZaposleniService;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Loading Data...");

        Preduzece p1 = new Preduzece();
        p1.setNaziv("ThinkStudio");
        p1.setPib("102979306");
        p1.setRacun("908‑10501‑97");
        p1.setAdresa("Držićeva 11");
        p1.setGrad("Beograd");
        p1.setTelefon("0112324444");
        p1.setEmail("office@thinkstudio.com");
        p1.setWebAdresa("thinkstudio.com");
        p1.setIsActive(true);

        Preduzece p2 = new Preduzece();
        p2.setNaziv("Universal UK");
        p2.setPib("1238483921");
        p2.setRacun("55743513");
        p2.setAdresa("22 Old Gloucester Street");
        p2.setGrad("London");
        p2.setTelefon("0113928422");
        p2.setEmail("london@universal.co.uk");
        p2.setWebAdresa("universal.co.uk");
        p2.setIsActive(true);
        p2.setKomentar("UK");

        Preduzece p3 = new Preduzece();
        p3.setNaziv("Blue Marble Inc");
        p3.setPib("194382931");
        p3.setRacun("908‑11501‑07");
        p3.setAdresa("Bulevar Mihajla Pupina 13");
        p3.setGrad("Beograd");
        p3.setTelefon("7975777666");
        p3.setEmail("office@bluemarble.com");
        p3.setWebAdresa("bluemarble.com");
        p3.setIsActive(true);

        Preduzece p4 = new Preduzece();
        p4.setNaziv("BP Production");
        p4.setPib("123-442-1134");
        p4.setRacun("132355513");
        p4.setAdresa("7115 3rd Ave");
        p4.setGrad("New York City");
        p4.setTelefon("555-1234");
        p4.setEmail("contact@bp.com");
        p4.setWebAdresa("bp.com");
        p4.setIsActive(true);
        p4.setKomentar("SAD");

        Preduzece p5 = new Preduzece();
        p5.setNaziv("Mark Cinema");
        p5.setPib("129438322");
        p5.setRacun("908‑14501‑28");
        p5.setAdresa("Bulevar Zorana Đinđića 11");
        p5.setGrad("Beograd");
        p5.setTelefon("0119303492");
        p5.setEmail("markcinema@gmail.com");
        p5.setWebAdresa("markcinema.rs");
        p5.setIsActive(false);

        Preduzece p6 = new Preduzece();
        p6.setNaziv("Fashion World");
        p6.setPib("193849293");
        p6.setRacun("908‑28311‑28");
        p6.setAdresa("Semjuela Beketa 55");
        p6.setGrad("Beograd");
        p6.setTelefon("011948293");
        p6.setEmail("office@fashionworld.com");
        p6.setWebAdresa("fashionworld.com");
        p6.setIsActive(false);

        Zaposleni z1 = new Zaposleni();
        z1.setIme("Darko");
        z1.setPrezime("Stanković");
        z1.setImeRoditelja("Miodrag");
        z1.setPocetakRadnogOdnosa(new Date(2018, Calendar.JANUARY, 22));
        z1.setJmbg("0311988710341");
        z1.setPol(PolZaposlenog.MUSKO);
        z1.setDatumRodjenja(new Date(1988, Calendar.NOVEMBER, 13));
        z1.setAdresa("Držićeva 5");
        z1.setGrad("Beograd");
        z1.setBrojRacuna("908‑10501‑97");
        z1.setStepenObrazovanja("5");
        z1.setBrojRadneKnjizice(62834L);
        z1.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);

        Plata pl1 = new Plata();
        pl1.setDatumOd(z1.getPocetakRadnogOdnosa());
        pl1.setNetoPlata(70000.00);
        pl1.setZaposleni(z1);

        Zaposleni z2 = new Zaposleni();
        z2.setIme("Marko");
        z2.setPrezime("Jovanović");
        z2.setImeRoditelja("Pavle");
        z2.setPocetakRadnogOdnosa(new Date(2018, Calendar.JANUARY, 22));
        z2.setJmbg("0502999710381");
        z2.setPol(PolZaposlenog.MUSKO);
        z2.setDatumRodjenja(new Date(1999, Calendar.FEBRUARY, 5));
        z2.setAdresa("Bulevar Nikole Tesle 33");
        z2.setGrad("Beograd");
        z2.setBrojRacuna("908‑10308‑97");
        z2.setStepenObrazovanja("5");
        z2.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        z2.setKomentar("omladinska");

        Plata pl2 = new Plata();
        pl2.setDatumOd(z2.getPocetakRadnogOdnosa());
        pl2.setNetoPlata(70000.00);
        pl2.setZaposleni(z2);

        Zaposleni z3 = new Zaposleni();
        z3.setIme("Bojana");
        z3.setPrezime("Šolak");
        z3.setImeRoditelja("Marko");
        z3.setPocetakRadnogOdnosa(new Date(2017, Calendar.MAY, 15));
        z3.setJmbg("0904978710699");
        z3.setPol(PolZaposlenog.ZENSKO);
        z3.setDatumRodjenja(new Date(1978, Calendar.APRIL, 9));
        z3.setAdresa("Trg Republike 4");
        z3.setGrad("Beograd");
        z3.setBrojRacuna("903‑14308‑97");
        z3.setStepenObrazovanja("6");
        z3.setBrojRadneKnjizice(33456L);
        z3.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);

        Plata pl3 = new Plata();
        pl3.setDatumOd(z3.getPocetakRadnogOdnosa());
        pl3.setNetoPlata(115300.00);
        pl3.setZaposleni(z3);

        Zaposleni z4 = new Zaposleni();
        z4.setIme("Darko");
        z4.setPrezime("Ognjenović");
        z4.setImeRoditelja("Aleksa");
        z4.setPocetakRadnogOdnosa(new Date(2019, Calendar.APRIL, 15));
        z4.setJmbg("0101995710121");
        z4.setPol(PolZaposlenog.MUSKO);
        z4.setDatumRodjenja(new Date(1995, Calendar.JANUARY, 1));
        z4.setAdresa("Masarikova 11");
        z4.setGrad("Beograd");
        z4.setBrojRacuna("903‑33308‑97");
        z4.setStepenObrazovanja("6");
        z4.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);

        Plata pl4 = new Plata();
        pl4.setDatumOd(z4.getPocetakRadnogOdnosa());
        pl4.setNetoPlata(230000.00);
        pl4.setZaposleni(z4);

        Zaposleni z5 = new Zaposleni();
        z5.setIme("Dimitrije");
        z5.setPrezime("Zdravković");
        z5.setImeRoditelja("Kosta");
        z5.setPocetakRadnogOdnosa(new Date(2021, Calendar.JANUARY, 1));
        z5.setJmbg("0711987710241");
        z5.setPol(PolZaposlenog.MUSKO);
        z5.setDatumRodjenja(new Date(1987, Calendar.NOVEMBER, 27));
        z5.setAdresa("Bulevar Zorana Đinđića 1");
        z5.setGrad("Beograd");
        z5.setBrojRacuna("933‑47345‑92");
        z5.setStepenObrazovanja("6");
        z5.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);

        Plata pl5 = new Plata();
        pl5.setDatumOd(z5.getPocetakRadnogOdnosa());
        pl5.setNetoPlata(110000.00);
        pl5.setZaposleni(z5);

        this.zaposleniRepository.save(z1);
        this.zaposleniRepository.save(z2);
        this.zaposleniRepository.save(z3);
        this.zaposleniRepository.save(z4);
        this.zaposleniRepository.save(z5);

        this.plataRepository.save(pl1);
        this.plataRepository.save(pl2);
        this.plataRepository.save(pl3);
        this.plataRepository.save(pl4);
        this.plataRepository.save(pl5);

        log.info("Data loaded!");
    }
}