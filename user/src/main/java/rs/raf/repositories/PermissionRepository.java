package rs.raf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
