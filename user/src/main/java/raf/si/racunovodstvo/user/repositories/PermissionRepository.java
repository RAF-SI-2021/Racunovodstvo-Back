package raf.si.racunovodstvo.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.user.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
