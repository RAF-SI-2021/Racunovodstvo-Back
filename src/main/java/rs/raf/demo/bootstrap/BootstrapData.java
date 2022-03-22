package rs.raf.demo.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(BootstrapData.class);
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
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


        log.info("Data loaded!");
    }
}
