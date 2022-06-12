package raf.si.racunovodstvo.knjizenje.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.model.*;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;
import raf.si.racunovodstvo.knjizenje.repositories.*;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;

import java.util.*;

@Component
public class BootstrapData implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(BootstrapData.class);
    private final FakturaRepository fakturaRepository;
    private final KontnaGrupaRepository kontnaGrupaRepository;
    private final KontoRepository kontoRepository;
    private final KnjizenjeRepository knjizenjeRepository;
    private final ProfitniCentarRepository profitniCentarRepository;
    private final TroskovniCentarRepository troskovniCentarRepository;
    private final BazniKontoRepository bazniKontoRepository;
    private final PovracajRepository povracajRepository;
    private final TransakcijaRepository transakcijaRepository;
    private final SifraTransakcijeRepository sifraTransakcijeRepository;


    @Autowired
    public BootstrapData(FakturaRepository fakturaRepository,
                         KontoRepository kontoRepository,
                         KontnaGrupaRepository kontnaGrupaRepository,
                         KnjizenjeRepository knjizenjeRepository,
                         TransakcijaRepository transakcijaRepository, SifraTransakcijeRepository sifraTransakcijeRepository,
                         ProfitniCentarRepository profitniCentarRepository,
                         TroskovniCentarRepository troskovniCentarRepository,
                         BazniKontoRepository bazniKontoRepository,
                         PovracajRepository povracajRepository
    ) {
        this.fakturaRepository = fakturaRepository;
        this.kontoRepository = kontoRepository;
        this.knjizenjeRepository = knjizenjeRepository;
        this.kontnaGrupaRepository = kontnaGrupaRepository;
        this.transakcijaRepository = transakcijaRepository;
        this.sifraTransakcijeRepository = sifraTransakcijeRepository;
        this.profitniCentarRepository = profitniCentarRepository;
        this.troskovniCentarRepository = troskovniCentarRepository;
        this.bazniKontoRepository = bazniKontoRepository;
        this.povracajRepository = povracajRepository;
    }

    private Faktura getDefaultFaktura() {

        Faktura f1 = new Faktura();
        f1.setBrojFakture("1");
        f1.setIznos(1000.00);
        f1.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        f1.setDatumIzdavanja(new Date());
        f1.setDatumPlacanja(new Date());
        f1.setKurs(117.8);
        f1.setNaplata(1000.00);
        f1.setPorez(10.00);
        f1.setPorezProcenat(1.00);
        f1.setProdajnaVrednost(1000.00);
        f1.setValuta("EUR");
        f1.setBrojDokumenta("1234");
        f1.setRokZaPlacanje(new Date());
        f1.setTipDokumenta(TipDokumenta.FAKTURA);

        return f1;
    }

    private Preduzece getDefaultPreduzece() {
        Preduzece p1 = new Preduzece();
        p1.setNaziv("Test Preduzece");
        p1.setPib("111222333");
        p1.setAdresa("test adresa");
        p1.setGrad("Beograd");

        return p1;
    }

    private Konto createKonto(KontnaGrupa kg, Knjizenje knj, Double duguje, Double potrazuje) {
        Konto konto = new Konto();
        konto.setDuguje(duguje);
        konto.setPotrazuje(potrazuje);
        konto.setKnjizenje(knj);
        konto.setKontnaGrupa(kg);
        return konto;
    }

    private SifraTransakcije getRandomSifraTransakcije() {
        SifraTransakcije st = new SifraTransakcije();
        st.setSifra(1010L);
        st.setNazivTransakcije("1010LLLLL");
        return st;
    }

    private Transakcija getRandomTransakcija() {
        Transakcija tr = new Transakcija();
        tr.setTipDokumenta(TipDokumenta.FAKTURA);
        tr.setIznos(222.33);
        tr.setTipTransakcije(TipTransakcije.UPLATA);
        tr.setDatumTransakcije(new Date());
        return tr;
    }

    private Povracaj createPovracaj(String brojPovracaja, Date datum, Double prodajnaVrednost) {
        Povracaj povracaj = new Povracaj();
        povracaj.setBrojPovracaja(brojPovracaja);
        povracaj.setDatumPovracaja(datum);
        povracaj.setProdajnaVrednost(prodajnaVrednost);

        return povracaj;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Loading Data...");

        Faktura f1 = getDefaultFaktura();
        f1.setBrojFakture("10");
        f1.setIznos(1000.00);
        f1.setBrojDokumenta("1233");
        f1.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        f1.setPreduzeceId(1L);

        Faktura f2 = getDefaultFaktura();
        f2.setBrojFakture("5");
        f2.setIznos(2000.00);
        f2.setBrojDokumenta("1235");
        f2.setPreduzeceId(2L);

        Faktura f3 = getDefaultFaktura();
        f3.setIznos(3000.00);
        f3.setPreduzeceId(2L);
        f3.setTipFakture(TipFakture.IZLAZNA_FAKTURA);

        Faktura f4 = getDefaultFaktura();
        f4.setBrojFakture("7");
        f4.setIznos(4000.00);
        f4.setBrojDokumenta("1237");
        f4.setPreduzeceId(2L);

        Faktura f5 = getDefaultFaktura();
        f5.setBrojFakture("9");
        f5.setIznos(3000.00);
        f5.setBrojDokumenta("1239");
        f5.setPreduzeceId(2L);
        f5.setTipFakture(TipFakture.IZLAZNA_FAKTURA);

        Faktura f6 = getDefaultFaktura();
        f6.setIznos(15000.00);
        f6.setBrojFakture("8");
        f6.setBrojDokumenta("123366");
        f6.setPreduzeceId(2L);
        f6.setTipFakture(TipFakture.MALOPRODAJNA_FAKTURA);

        Faktura f7 = getDefaultFaktura();
        f7.setIznos(20000.00);
        f7.setBrojFakture("7");
        f7.setBrojFakture("33SSSS9");
        f7.setBrojDokumenta("1233222");
        f7.setPreduzeceId(2L);
        f7.setTipFakture(TipFakture.MALOPRODAJNA_FAKTURA);

        this.fakturaRepository.save(f1);
        this.fakturaRepository.save(f2);
        this.fakturaRepository.save(f3);
        this.fakturaRepository.save(f4);
        this.fakturaRepository.save(f5);
        this.fakturaRepository.save(f6);
        this.fakturaRepository.save(f7);

        KontnaGrupa kg1 = new KontnaGrupa();
        kg1.setBrojKonta("0");
        kg1.setNazivKonta("Naziv kontne grupe 0");
        KontnaGrupa kg2 = new KontnaGrupa();
        kg2.setBrojKonta("1");
        kg2.setNazivKonta("Naziv kontne grupe 1");
        KontnaGrupa kg3 = new KontnaGrupa();
        kg3.setBrojKonta("01");
        kg3.setNazivKonta("Naziv kontne grupe 01");
        KontnaGrupa kg4 = new KontnaGrupa();
        kg4.setBrojKonta("010");
        kg4.setNazivKonta("Naziv kontne grupe 010");
        KontnaGrupa kg5 = new KontnaGrupa();
        kg5.setBrojKonta("0101");
        kg5.setNazivKonta("Naziv kontne grupe 0101");
        KontnaGrupa kg6 = new KontnaGrupa();
        kg6.setBrojKonta("01011");
        kg6.setNazivKonta("Naziv kontne grupe 01011");
        KontnaGrupa kg7 = new KontnaGrupa();
        kg7.setBrojKonta("12");
        kg7.setNazivKonta("Naziv kontne grupe 12");
        KontnaGrupa kg8 = new KontnaGrupa();
        kg8.setBrojKonta("10");
        kg8.setNazivKonta("Naziv kontne grupe 10");
        KontnaGrupa kg9 = new KontnaGrupa();
        kg9.setBrojKonta("2");
        kg9.setNazivKonta("Naziv kontne grupe 2");
        KontnaGrupa kg10 = new KontnaGrupa();
        kg10.setBrojKonta("02");
        kg10.setNazivKonta("Naziv kontne grupe 02");
        KontnaGrupa kg11 = new KontnaGrupa();
        kg11.setBrojKonta("21");
        kg11.setNazivKonta("Naziv kontne grupe 21");
        KontnaGrupa kg12 = new KontnaGrupa();
        kg12.setBrojKonta("120");
        kg12.setNazivKonta("Naziv kontne grupe 120");
        KontnaGrupa kg13 = new KontnaGrupa();
        kg13.setBrojKonta("00");
        kg13.setNazivKonta("Naziv kontne grupe 00");
        KontnaGrupa kg14 = new KontnaGrupa();
        kg14.setBrojKonta("000");
        kg14.setNazivKonta("Naziv kontne grupe 000");
        KontnaGrupa kg15 = new KontnaGrupa();
        kg15.setBrojKonta("0001");
        kg15.setNazivKonta("Naziv kontne grupe 0001");
        KontnaGrupa kg16 = new KontnaGrupa();
        kg16.setBrojKonta("0001");
        kg16.setNazivKonta("Naziv kontne grupe 0001");
        this.kontnaGrupaRepository.saveAll(Arrays.asList(kg1, kg2, kg3, kg4, kg5, kg6, kg7, kg8, kg9, kg10, kg11, kg12, kg13, kg14, kg15, kg16));
        KontnaGrupa kg3t = new KontnaGrupa();
        kg3t.setBrojKonta("3");
        kg3t.setNazivKonta("Naziv kontne grupe 3");
        KontnaGrupa kg30 = new KontnaGrupa();
        kg30.setBrojKonta("30");
        kg30.setNazivKonta("Naziv kontne grupe 30");
        KontnaGrupa kg301 = new KontnaGrupa();
        kg301.setBrojKonta("301");
        kg301.setNazivKonta("Naziv kontne grupe 301");
        KontnaGrupa kg302 = new KontnaGrupa();
        kg302.setBrojKonta("302");
        kg302.setNazivKonta("Naziv kontne grupe 302");
        KontnaGrupa kg306 = new KontnaGrupa();
        kg306.setBrojKonta("306");
        kg306.setNazivKonta("Naziv kontne grupe 306");
        KontnaGrupa kg309 = new KontnaGrupa();
        kg309.setBrojKonta("309");
        kg309.setNazivKonta("Naziv kontne grupe 309");
        KontnaGrupa kg31 = new KontnaGrupa();
        kg31.setBrojKonta("31");
        kg31.setNazivKonta("Naziv kontne grupe 31");
        KontnaGrupa kg32 = new KontnaGrupa();
        kg32.setBrojKonta("32");
        kg32.setNazivKonta("Naziv kontne grupe 32");
        KontnaGrupa kg33 = new KontnaGrupa();
        kg33.setBrojKonta("33");
        kg33.setNazivKonta("Naziv kontne grupe 33");
        KontnaGrupa kg34 = new KontnaGrupa();
        kg34.setBrojKonta("34");
        kg34.setNazivKonta("Naziv kontne grupe 34");
        KontnaGrupa kg35 = new KontnaGrupa();
        kg35.setBrojKonta("35");
        kg35.setNazivKonta("Naziv kontne grupe 35");
        this.kontnaGrupaRepository.saveAll(Arrays.asList(kg3t, kg30, kg301, kg302, kg306, kg309, kg31, kg32, kg33, kg34, kg35));


        Knjizenje knj1 = new Knjizenje();
        knj1.setBrojNaloga("N123S3");
        knj1.setDatumKnjizenja(new Date());
        knj1.setDokument(f1);
        Knjizenje knj2 = new Knjizenje();
        knj2.setDatumKnjizenja(new Date());
        knj2.setBrojNaloga("N123FF3");
        knj2.setDokument(f1);
        Knjizenje knj3 = new Knjizenje();
        knj3.setDatumKnjizenja(new Date());
        knj3.setDokument(f2);
        Knjizenje knj4 = new Knjizenje();
        knj4.setDatumKnjizenja(new Date());
        knj4.setDokument(f2);
        knj3.setBrojNaloga("N13S3");
        knj4.setBrojNaloga("N23FF3");
        this.knjizenjeRepository.save(knj1);
        this.knjizenjeRepository.save(knj2);
        this.knjizenjeRepository.save(knj3);
        this.knjizenjeRepository.save(knj4);

        Konto k1 = createKonto(kg1, knj1, 1299.0, 900.0);
        Konto k2 = createKonto(kg2, knj2, 1300.0, 848.0);
        Konto k3 = createKonto(kg3, knj3, 700.0, 940.0);
        Konto k4 = createKonto(kg4, knj4, 1000.0, 504.0);
        Konto k5 = createKonto(kg5, knj1, 1003.0, 203.0);
        Konto k6 = createKonto(kg6, knj2, 200.0, 504.0);
        Konto k7 = createKonto(kg7, knj4, 2311.0, 2003.0);
        Konto k8 = createKonto(kg8, knj3, 100.0, 504.0);
        Konto k9 = createKonto(kg9, knj2, 450.0, 304.0);
        Konto k10 = createKonto(kg10, knj3, 1030.0, 584.0);
        Konto k11 = createKonto(kg11, knj3, 1020.0, 704.0);
        Konto k12 = createKonto(kg12, knj3, 1700.0, 1504.0);
        Konto k13 = createKonto(kg1, knj4, 1090.0, 1004.0);
        Konto k14 = createKonto(kg1, knj4, 1200.0, 1504.0);
        Konto k15 = createKonto(kg11, knj4, 1430.0, 1594.0);
        Konto k16 = createKonto(kg8, knj4, 1000.0, 504.0);
        Konto k17 = createKonto(kg13, knj4, 1090.0, 1004.0);
        Konto k18 = createKonto(kg14, knj4, 1200.0, 1504.0);
        Konto k19 = createKonto(kg15, knj4, 1430.0, 1594.0);
        Konto k20 = createKonto(kg16, knj4, 1000.0, 504.0);

        this.kontoRepository.saveAll(Arrays.asList(k1, k2, k3, k4, k5, k6, k7, k8, k9, k10, k11, k12, k13, k14, k15, k16, k17, k18, k19, k20));

        Knjizenje knj2020 = new Knjizenje();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 5, 5);
        knj2020.setDatumKnjizenja(calendar.getTime());
        knj2020.setDokument(f2);
        knj2020.setBrojNaloga("N2020FF");
        this.knjizenjeRepository.save(knj2020);

        Knjizenje knj2021 = new Knjizenje();
        calendar = Calendar.getInstance();
        calendar.set(2021, 5, 5);
        knj2021.setDatumKnjizenja(calendar.getTime());
        knj2021.setDokument(f2);
        knj2021.setBrojNaloga("N2020FF");
        this.knjizenjeRepository.save(knj2021);

        Konto k3t = createKonto(kg3t, knj2020, 1299.0, 900.0);
        Konto k30 = createKonto(kg30, knj2020, 1300.0, 848.0);
        Konto k301 = createKonto(kg301, knj2020, 700.0, 940.0);
        Konto k302 = createKonto(kg302, knj2020, 1000.0, 504.0);
        Konto k306 = createKonto(kg306, knj2020, 1003.0, 203.0);
        Konto k309 = createKonto(kg309, knj2020, 200.0, 504.0);
        Konto k31 = createKonto(kg31, knj2020, 2311.0, 2003.0);
        Konto k32 = createKonto(kg32, knj2020, 100.0, 504.0);
        Konto k33 = createKonto(kg33, knj2020, 450.0, 304.0);
        Konto k34 = createKonto(kg34, knj2020, 1030.0, 584.0);
        Konto k35 = createKonto(kg35, knj2020, 1020.0, 704.0);
        Konto k3t2 = createKonto(kg3, knj2021, 1700.0, 1504.0);
        Konto k3012 = createKonto(kg301, knj2021, 1090.0, 1004.0);
        Konto k3022 = createKonto(kg302, knj2021, 1200.0, 1504.0);
        Konto k3062 = createKonto(kg306, knj2021, 1430.0, 1594.0);
        Konto k3092 = createKonto(kg309, knj2021, 1000.0, 504.0);
        Konto k312 = createKonto(kg31, knj2021, 1090.0, 1004.0);
        Konto k322 = createKonto(kg32, knj2021, 1200.0, 1504.0);
        Konto k332 = createKonto(kg33, knj2021, 1430.0, 1594.0);
        Konto k342 = createKonto(kg34, knj2021, 1000.0, 504.0);

        this.kontoRepository.saveAll(Arrays.asList(k3t, k30, k301, k302, k306, k309, k31, k32, k33, k34, k35));
        this.kontoRepository.saveAll(Arrays.asList(k3t2, k302, k3012, k3022, k3062, k3092, k312, k322, k332, k342));
        this.kontoRepository.saveAll(Arrays.asList(k1, k2, k3, k4, k5, k6, k7, k8, k9, k10, k11, k12, k13, k14, k15, k16, k17, k18, k19, k20));

        Konto konto1 = new Konto();
        konto1.setDuguje(1000.0);
        konto1.setPotrazuje(500.0);
        konto1.setKnjizenje(knj1);
        konto1.setKontnaGrupa(kg1);
        konto1 = kontoRepository.save(konto1);

        Konto konto2 = new Konto();
        konto2.setDuguje(2000.0);
        konto2.setKnjizenje(knj1);
        konto2.setKontnaGrupa(kg1);
        konto2.setPotrazuje(1000.0);
        kontoRepository.save(konto2);

        Konto konto3 = new Konto();
        konto3.setDuguje(0.0);
        konto3.setKnjizenje(knj1);
        konto3.setKontnaGrupa(kg1);
        konto3.setPotrazuje(1000.0);
        kontoRepository.save(konto3);

        Knjizenje knjizenje = new Knjizenje();

        kontoRepository.save(konto1);
        knjizenje.setKnjizenjeId(1L);
        knjizenje.setKonto(List.of(konto1, konto2, konto3));
        knjizenje.setDatumKnjizenja(new Date());
        knjizenje.setBrojNaloga("N 1234");
        knjizenjeRepository.save(knjizenje);
        konto1.setKnjizenje(knjizenje);
        konto2.setKnjizenje(knjizenje);
        konto3.setKnjizenje(knjizenje);
        kontoRepository.save(konto1);

        ProfitniCentar profitniCentar = new ProfitniCentar();
        profitniCentar.setUkupniTrosak(100.00);
        profitniCentar.setNaziv("Profitni centar 1");
        profitniCentar.setLokacijaId(1l);
        profitniCentar.setSifra("1");
        profitniCentar.setOdgovornoLiceId(1l);
        profitniCentarRepository.save(profitniCentar);

        ProfitniCentar profitniCentar2 = new ProfitniCentar();
        profitniCentar2.setUkupniTrosak(100.00);
        profitniCentar2.setNaziv("Profitni centar 2");
        profitniCentar2.setLokacijaId(1l);
        profitniCentar2.setSifra("12");
        profitniCentar2.setOdgovornoLiceId(1l);
        profitniCentar2.setParentProfitniCentar(profitniCentar);
        profitniCentarRepository.save(profitniCentar2);

        ProfitniCentar profitniCentar3 = new ProfitniCentar();
        profitniCentar3.setUkupniTrosak(500.00);
        profitniCentar3.setNaziv("Profitni centar 3");
        profitniCentar3.setLokacijaId(1l);
        profitniCentar3.setSifra("123");
        profitniCentar3.setOdgovornoLiceId(1l);
        profitniCentar3.setParentProfitniCentar(profitniCentar);
        profitniCentarRepository.save(profitniCentar3);

        ProfitniCentar profitniCentar4 = new ProfitniCentar();
        profitniCentar4.setUkupniTrosak(100.00);
        profitniCentar4.setNaziv("Profitni centar 4");
        profitniCentar4.setLokacijaId(1l);
        profitniCentar4.setSifra("1234");
        profitniCentar4.setOdgovornoLiceId(1l);
        profitniCentar4.setParentProfitniCentar(profitniCentar3);
        profitniCentarRepository.save(profitniCentar4);

        TroskovniCentar troskovniCentar = new TroskovniCentar();
        troskovniCentar.setUkupniTrosak(500.00);
        troskovniCentar.setNaziv("Troskovni centar 1");
        troskovniCentar.setLokacijaId(1l);
        troskovniCentar.setSifra("12345");
        troskovniCentar.setOdgovornoLiceId(1l);
        BazniKonto bazniKonto = new BazniKonto();
        bazniKonto.setDuguje(0.0);
        bazniKonto.setBrojNalogaKnjizenja(knj1.getBrojNaloga());
        bazniKonto.setDatumKnjizenja(knj1.getDatumKnjizenja());
        bazniKonto.setKomentarKnjizenja(knj1.getKomentar());
        bazniKonto.setKontnaGrupa(kg1);
        bazniKonto.setPotrazuje(1000.0);
        bazniKonto.setBazniCentar(troskovniCentar);
        troskovniCentar.setKontoList(List.of(bazniKonto));
        troskovniCentarRepository.save(troskovniCentar);
        bazniKontoRepository.save(bazniKonto);

        Povracaj povracaj1 = this.createPovracaj("123", new Date(), 2000.00);
        povracajRepository.save(povracaj1);
        Povracaj povracaj2 = this.createPovracaj("321", new Date(), 1100.00);
        povracajRepository.save(povracaj2);

        SifraTransakcije st = getRandomSifraTransakcije();
        sifraTransakcijeRepository.save(st);

        Transakcija tr1 = getRandomTransakcija();
        tr1.setBrojDokumenta("1122");
        tr1.setBrojTransakcije("1123L");
        tr1.setSifraTransakcije(st);
        tr1.setPreduzeceId(1L);
        Transakcija tr2 = getRandomTransakcija();
        tr2.setBrojDokumenta("1331");
        tr2.setBrojTransakcije("3312L");
        tr2.setSifraTransakcije(st);
        tr2.setPreduzeceId(1L);
        Transakcija tr3 = getRandomTransakcija();
        tr3.setBrojDokumenta("1389");
        tr3.setBrojTransakcije("1492L");
        tr3.setSifraTransakcije(st);
        tr3.setPreduzeceId(1L);
        transakcijaRepository.save(tr1);
        transakcijaRepository.save(tr2);
        transakcijaRepository.save(tr3);

        SifraTransakcije st1 = new SifraTransakcije();
        st1.setSifra(1121L);
        st1.setNazivTransakcije("banana");
        SifraTransakcije st2 = new SifraTransakcije();
        st2.setSifra(3121L);
        st2.setNazivTransakcije("ananas");
        SifraTransakcije st3 = new SifraTransakcije();
        st3.setSifra(4121L);
        st3.setNazivTransakcije("kajsija");
        sifraTransakcijeRepository.save(st1);
        sifraTransakcijeRepository.save(st2);
        sifraTransakcijeRepository.save(st3);

        log.info("Data loaded!");
    }
}
