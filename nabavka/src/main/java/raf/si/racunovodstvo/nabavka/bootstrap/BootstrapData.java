package raf.si.racunovodstvo.nabavka.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.model.Kalkulacija;
import raf.si.racunovodstvo.nabavka.model.KalkulacijaArtikal;
import raf.si.racunovodstvo.nabavka.model.Konverzija;
import raf.si.racunovodstvo.nabavka.model.KonverzijaArtikal;
import raf.si.racunovodstvo.nabavka.model.Lokacija;
import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;
import raf.si.racunovodstvo.nabavka.model.enums.TipKalkulacije;
import raf.si.racunovodstvo.nabavka.repositories.ArtikalRepository;
import raf.si.racunovodstvo.nabavka.repositories.KalkulacijaRepository;
import raf.si.racunovodstvo.nabavka.repositories.KonverzijaRepository;
import raf.si.racunovodstvo.nabavka.repositories.LokacijaRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private KalkulacijaRepository kalkulacijaRepository;
    private LokacijaRepository lokacijaRepository;
    private ArtikalRepository artikalRepository;
    private KonverzijaRepository konverzijaRepository;

    public BootstrapData(KalkulacijaRepository kalkulacijaRepository,
                         LokacijaRepository lokacijaRepository,
                         ArtikalRepository artikalRepository,
                         KonverzijaRepository konverzijaRepository) {
        this.kalkulacijaRepository = kalkulacijaRepository;
        this.lokacijaRepository = lokacijaRepository;
        this.artikalRepository = artikalRepository;
        this.konverzijaRepository = konverzijaRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Kalkulacija k1 = napraviDefaultKalkulaciju("AAA", "Vrsac");
        k1.setLokacija(new Lokacija());
        Lokacija l1 = new Lokacija();
        l1.setAdresa("Beograd");
        l1.setNaziv("BG naziv");
        lokacijaRepository.save(l1);
        k1.setLokacija(l1);
        kalkulacijaRepository.save(k1);

        Kalkulacija k2 = napraviDefaultKalkulaciju("BBB", "Subotica");
        l1 = new Lokacija();
        l1.setAdresa("Novi Sad");
        l1.setNaziv("NS naziv");
        lokacijaRepository.save(l1);
        k1.setLokacija(l1);
        kalkulacijaRepository.save(k2);

        Kalkulacija k3 = napraviDefaultKalkulaciju("CCC", "Pirot");
        TroskoviNabavke troskoviNabavke = new TroskoviNabavke();
        troskoviNabavke.setNaziv("Test");
        troskoviNabavke.setCena(100.0);
        k3.setTroskoviNabavke(List.of(troskoviNabavke));
        l1 = new Lokacija();
        l1.setAdresa("Kragujevac");
        l1.setNaziv("KG naziv");
        lokacijaRepository.save(l1);
        kalkulacijaRepository.save(k3);

        Konverzija konverzija = new Konverzija();
        konverzija.setBrojKonverzije("ABC");
        konverzija.setTroskoviNabavke(new ArrayList<>());
        l1 = new Lokacija();
        l1.setAdresa("Leskovac");
        l1.setNaziv("LE naziv");
        l1 = lokacijaRepository.save(l1);
        konverzija.setLokacija(l1);
        konverzija.setDatum(new Date());
        konverzija.setDobavljacId(1L);
        konverzija.setArtikli(new ArrayList<>());
        konverzija.setFakturnaCena(100D);
        konverzija.setNabavnaVrednost(200D);
        konverzija.setValuta("RSD");
        konverzijaRepository.save(konverzija);

        KonverzijaArtikal konverzijaArtikal = new KonverzijaArtikal();
        konverzijaArtikal.setSifraArtikla("SIFRA");
        konverzijaArtikal.setKolicina(1);
        konverzijaArtikal.setNabavnaCena(1.0);
        konverzijaArtikal.setNazivArtikla("NAZIV");
        konverzijaArtikal.setJedinicaMere("MERA");
        konverzijaArtikal.setRabat(2.0);
        konverzijaArtikal.setRabatProcenat(10.0);
        konverzijaArtikal.setUkupnaNabavnaVrednost(2.0);
        konverzijaArtikal.setNabavnaCenaPosleRabata(1.0);
        konverzijaArtikal.setBaznaKonverzijaKalkulacija(konverzija);
        artikalRepository.save(konverzijaArtikal);

        KalkulacijaArtikal kalkulacijaArtikal1 = new KalkulacijaArtikal();
        kalkulacijaArtikal1.setKolicina(1);
        kalkulacijaArtikal1.setNabavnaCena(1.0);
        kalkulacijaArtikal1.setSifraArtikla("SIFRA2");
        kalkulacijaArtikal1.setNazivArtikla("NAZIV2");
        kalkulacijaArtikal1.setJedinicaMere("MERA");
        kalkulacijaArtikal1.setRabat(2.0);
        kalkulacijaArtikal1.setRabatProcenat(10.0);
        kalkulacijaArtikal1.setUkupnaNabavnaVrednost(2.0);
        kalkulacijaArtikal1.setNabavnaCenaPosleRabata(1.0);
        kalkulacijaArtikal1.setMarza(3.0);
        kalkulacijaArtikal1.setMarzaProcenat(20.0);
        kalkulacijaArtikal1.setOsnovica(19.0);
        kalkulacijaArtikal1.setPorez(10.0);
        kalkulacijaArtikal1.setPorezProcenat(10.0);
        kalkulacijaArtikal1.setProdajnaCena(30.0);
        kalkulacijaArtikal1.setUkupnaProdajnaVrednost(45.0);
        kalkulacijaArtikal1.setProdajnaOsnovica(40.0);
        kalkulacijaArtikal1.setBaznaKonverzijaKalkulacija(k1);
        artikalRepository.save(kalkulacijaArtikal1);

        napraviDefaultArtikal(k1, "SIFRA123", "KOMAD", 20);
        napraviDefaultArtikal(k1, "SIFRA222", "KOMAD", 150);
        napraviDefaultArtikal(k1, "SIFRA333", "LITAR", 10);

    }

    private Kalkulacija napraviDefaultKalkulaciju(String brojKalkulacije, String adresa) {
        Lokacija l1 = new Lokacija();
        l1.setAdresa(adresa);
        l1.setNaziv(adresa + " naziv");
        l1 = lokacijaRepository.save(l1);

        Kalkulacija kalkulacija = new Kalkulacija();
        kalkulacija.setBrojKalkulacije(brojKalkulacije);
        kalkulacija.setTipKalkulacije(TipKalkulacije.VELEPRODAJA);
        kalkulacija.setTroskoviNabavke(new ArrayList<>());
        kalkulacija.setLokacija(l1);
        kalkulacija.setDatum(new Date());
        kalkulacija.setDobavljacId(1L);
        kalkulacija.setArtikli(new ArrayList<>());
        kalkulacija.setFakturnaCena(100D);
        kalkulacija.setNabavnaVrednost(200D);
        kalkulacija.setProdajnaCena(300D);
        kalkulacija.setValuta("RSD");
        return kalkulacija;
    }

    private void napraviDefaultArtikal(Kalkulacija k1, String sifra, String jedinicaMere, int kolicina) {
        KalkulacijaArtikal kalkulacijaArtikal = new KalkulacijaArtikal();
        kalkulacijaArtikal.setKolicina(kolicina);
        kalkulacijaArtikal.setNabavnaCena(1.0);
        kalkulacijaArtikal.setSifraArtikla(sifra);
        kalkulacijaArtikal.setNazivArtikla(sifra);
        kalkulacijaArtikal.setJedinicaMere(jedinicaMere);
        kalkulacijaArtikal.setRabat(2.0);
        kalkulacijaArtikal.setRabatProcenat(10.0);
        kalkulacijaArtikal.setUkupnaNabavnaVrednost(2.0);
        kalkulacijaArtikal.setNabavnaCenaPosleRabata(1.0);
        kalkulacijaArtikal.setMarza(3.0);
        kalkulacijaArtikal.setMarzaProcenat(20.0);
        kalkulacijaArtikal.setOsnovica(19.0);
        kalkulacijaArtikal.setPorez(10.0);
        kalkulacijaArtikal.setPorezProcenat(10.0);
        kalkulacijaArtikal.setProdajnaCena(30.0);
        kalkulacijaArtikal.setUkupnaProdajnaVrednost(45.0);
        kalkulacijaArtikal.setProdajnaOsnovica(40.0);
        kalkulacijaArtikal.setBaznaKonverzijaKalkulacija(k1);
        artikalRepository.save(kalkulacijaArtikal);
    }
}
