package rs.raf.demo.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.model.enums.PolZaposlenog;
import rs.raf.demo.model.enums.RadnaPozicija;
import rs.raf.demo.model.enums.StatusZaposlenog;
import rs.raf.demo.model.enums.TipDokumenta;
import rs.raf.demo.model.enums.TipFakture;
import rs.raf.demo.repositories.*;

import java.util.*;

@Component
public class BootstrapData implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(BootstrapData.class);
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final FakturaRepository fakturaRepository;
    private final PreduzeceRepository preduzeceRepository;
    private final PasswordEncoder passwordEncoder;
    private final KontnaGrupaRepository kontnaGrupaRepository;
    private final KontoRepository kontoRepository;
    private final KnjizenjeRepository knjizenjeRepository;
    private final ZaposleniRepository zaposleniRepository;
    private final StazRepository stazRepository;
    private final PlataRepository plataRepository;

    @Autowired
    public BootstrapData(UserRepository userRepository,
                         FakturaRepository fakturaRepository,
                         PermissionRepository permissionRepository,
                         PasswordEncoder passwordEncoder,
                         KontoRepository kontoRepository,
                         KontnaGrupaRepository kontnaGrupaRepository,
                         KnjizenjeRepository knjizenjeRepository,
                         PreduzeceRepository preduzeceRepository,
                         ZaposleniRepository zaposleniRepository,
                         StazRepository stazRepository,
                         PlataRepository plataRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.fakturaRepository = fakturaRepository;
        this.preduzeceRepository = preduzeceRepository;
        this.passwordEncoder = passwordEncoder;
        this.kontoRepository = kontoRepository;
        this.knjizenjeRepository = knjizenjeRepository;
        this.kontnaGrupaRepository = kontnaGrupaRepository;
        this.zaposleniRepository = zaposleniRepository;
        this.stazRepository = stazRepository;
        this.plataRepository = plataRepository;
    }

    private Preduzece getDefaultPreduzece(){
        Preduzece p1 = new Preduzece();
        p1.setNaziv("Test Preduzece");
        p1.setPib("111222333");
        p1.setAdresa("test adresa");
        p1.setGrad("Beograd");

        return p1;
    }
    private Faktura getDefaultFaktura(){


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

    @Override
    public void run(String... args) throws Exception {

        log.info("Loading Data...");

        Permission permission1 = new Permission();
        permission1.setName("permission1");
        permissionRepository.save(permission1);

        Permission permission2 = new Permission();
        permission2.setName("permission2");
        permissionRepository.save(permission2);

        Permission permission3 = new Permission();
        permission3.setName("permission3");
        permissionRepository.save(permission3);

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(this.passwordEncoder.encode("user1"));
        user1.setFirstName("Petar");
        user1.setLastName("Petrovic");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(this.passwordEncoder.encode("user2"));
        user2.setFirstName("Marko");
        user2.setLastName("Markovic");

        List<Permission> user1Permissions = new ArrayList<>();
        user1Permissions.add(permission1);
        user1Permissions.add(permission2);
        user1Permissions.add(permission3);
        user1.setPermissions(user1Permissions);

        this.userRepository.save(user1);
        this.userRepository.save(user2);

        Preduzece p1 = getDefaultPreduzece();
        Preduzece p2 = getDefaultPreduzece();

        this.preduzeceRepository.save(p1);
        this.preduzeceRepository.save(p2);

        Faktura f1 = getDefaultFaktura();
        f1.setIznos(1000.00);
        f1.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        f1.setPreduzece(p1);

        Faktura f2 = getDefaultFaktura();
        f2.setIznos(2000.00);
        f2.setPreduzece(p2);

        Faktura f3 = getDefaultFaktura();
        f3.setIznos(3000.00);
        f3.setTipFakture(TipFakture.IZLAZNA_FAKTURA);

        Faktura f4 = getDefaultFaktura();
        f4.setIznos(4000.00);

        Faktura f5 = getDefaultFaktura();
        f5.setIznos(3000.00);
        f5.setTipFakture(TipFakture.IZLAZNA_FAKTURA);

        this.fakturaRepository.save(f1);
        this.fakturaRepository.save(f2);
        this.fakturaRepository.save(f3);
        this.fakturaRepository.save(f4);
        this.fakturaRepository.save(f5);

        KontnaGrupa kg1 = new KontnaGrupa();
        kg1.setBrojKonta("0");
        kg1.setNazivKonta("Naziv kontne grupe 0");
        KontnaGrupa kg2 = new KontnaGrupa();
        kg2.setBrojKonta("1");
        kg2.setNazivKonta("Naziv kontne grupe 1");
        this.kontnaGrupaRepository.save(kg1);
        this.kontnaGrupaRepository.save(kg2);

        Knjizenje knj1 = new Knjizenje();
        knj1.setDatumKnjizenja(new Date());
        Knjizenje knj2 = new Knjizenje();
        knj1.setBrojNaloga("N123S3");
        knj2.setDatumKnjizenja(new Date());
        knj2.setBrojNaloga("N123FF3");
        this.knjizenjeRepository.save(knj1);
        this.knjizenjeRepository.save(knj2);

        Konto k1 = new Konto();
        k1.setDuguje(200.0);
        k1.setPotrazuje(300.0);
        k1.setKontnaGrupa(kg1);
        k1.setKnjizenje(knj1);
        Konto k2 = new Konto();
        k2.setDuguje(800.0);
        k2.setPotrazuje(1300.0);
        k2.setKontnaGrupa(kg2);
        k2.setKnjizenje(knj1);
        Konto k3 = new Konto();
        k3.setDuguje(500.0);
        k3.setPotrazuje(300.0);
        k3.setKontnaGrupa(kg1);
        k3.setKnjizenje(knj2);
        Konto k4 = new Konto();
        k4.setDuguje(1200.0);
        k4.setPotrazuje(300.0);
        k4.setKontnaGrupa(kg2);
        k4.setKnjizenje(knj2);

        this.kontoRepository.saveAll(Arrays.asList(k1, k2, k3, k4));

        Konto konto1 = new Konto();
        konto1.setDuguje(1000.0);
        konto1.setPotrazuje(500.0);
        konto1 = kontoRepository.save(konto1);

        Konto konto2 = new Konto();
        konto2.setDuguje(2000.0);
        konto2.setPotrazuje(1000.0);
        kontoRepository.save(konto2);

        Konto konto3 = new Konto();
        konto3.setDuguje(0.0);
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

        Staz staz = new Staz();
        staz.setPocetakRada(new Date());
        staz.setKrajRada(null);
        stazRepository.save(staz);

        List<Staz> stazevi = new ArrayList<>();
        stazevi.add(staz);

        Zaposleni zaposleni = new Zaposleni();
        zaposleni.setIme("Marko");
        zaposleni.setPrezime("Markovic");
        zaposleni.setPocetakRadnogOdnosa(new Date());
        zaposleni.setJmbg("1234567890123");
        zaposleni.setPol(PolZaposlenog.MUSKO);
        zaposleni.setStatusZaposlenog(StatusZaposlenog.ZAPOSLEN);
        zaposleni.setDatumRodjenja(new Date());
        zaposleni.setRadnaPozicija(RadnaPozicija.DIREKTOR);
        zaposleni.setStaz(stazevi);
        zaposleniRepository.save(zaposleni);

        Plata plata = new Plata();
        plata.setNetoPlata(100000.0);
        plata.setZaposleni(zaposleni);
        plata.setDatum(new Date());
        plataRepository.save(plata);


        log.info("Data loaded!");
    }
}
