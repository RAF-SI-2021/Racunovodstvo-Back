package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final PreduzeceRepository preduzeceRepository;
    private final FakturaRepository fakturaRepository;

    @Autowired
    public BootstrapData(UserRepository userRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder, PreduzeceRepository preduzeceRepository, FakturaRepository fakturaRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
        this.preduzeceRepository = preduzeceRepository;
        this.fakturaRepository = fakturaRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

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

        List<Permission> user1Permissions = new ArrayList<Permission>();
        user1Permissions.add(permission1);
        user1Permissions.add(permission2);
        user1Permissions.add(permission3);
        user1.setPermissions(user1Permissions);

        this.userRepository.save(user1);
        this.userRepository.save(user2);

        Preduzece preduzece = new Preduzece("Test Preduzece", 5125151, "Kneza Mihaila 6", "Beograd");
        preduzeceRepository.save(preduzece);
        Faktura faktura = new Faktura();
        faktura.setBrojFakture("safas324523");
        faktura.setPreduzece(preduzece);
        faktura.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fakturaRepository.save(faktura);
        Faktura faktura2 = new Faktura();
        faktura2.setBrojFakture("12345324523");
        faktura2.setPreduzece(preduzece);
        faktura2.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        fakturaRepository.save(faktura2);
        System.out.println("Data loaded!");
    }
}
