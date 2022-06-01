package raf.si.racunovodstvo.user.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.user.model.Permission;
import raf.si.racunovodstvo.user.model.PermissionType;
import raf.si.racunovodstvo.user.model.User;
import raf.si.racunovodstvo.user.repositories.PermissionRepository;
import raf.si.racunovodstvo.user.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(BootstrapData.class);
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(
            UserRepository userRepository,
            PermissionRepository permissionRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        log.info("Loading Data...");

        List<Permission> user1Permissions = new ArrayList<>();

        for (PermissionType value: PermissionType.values()) {
            Permission permission = new Permission();
            permission.setName(value.label);
            user1Permissions.add(permissionRepository.save(permission));
        }

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(this.passwordEncoder.encode("user1"));
        user1.setFirstName("Petar");
        user1.setLastName("Petrovic");
        user1.setEmail("ppetrovic@raf.rs");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(this.passwordEncoder.encode("user2"));
        user2.setFirstName("Marko");
        user2.setLastName("Markovic");
        user2.setEmail("mmarkovic@raf.rs");

        user1.setPermissions(user1Permissions);

        this.userRepository.save(user1);
        this.userRepository.save(user2);


        log.info("Data loaded!");
    }
}
