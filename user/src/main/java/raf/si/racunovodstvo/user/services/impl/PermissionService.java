package raf.si.racunovodstvo.user.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.user.constants.RedisConstants;
import raf.si.racunovodstvo.user.model.Permission;
import raf.si.racunovodstvo.user.repositories.PermissionRepository;
import raf.si.racunovodstvo.user.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IService<Permission, Long> {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    @CachePut(value = RedisConstants.PERMISSION_CACHE, key = "#result.id")
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    @Cacheable(value = RedisConstants.PERMISSION_CACHE, key = "#id")
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    @Cacheable(value = RedisConstants.PERMISSION_CACHE)
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    @CacheEvict(value = RedisConstants.PERMISSION_CACHE, key = "#id")
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }
}

