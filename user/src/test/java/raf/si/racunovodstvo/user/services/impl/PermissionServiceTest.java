package raf.si.racunovodstvo.user.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.user.model.Permission;
import raf.si.racunovodstvo.user.repositories.PermissionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @InjectMocks
    private PermissionService permissionService;
    @Mock
    private PermissionRepository permissionRepository;

    private static final Long MOCK_ID = 1L;


    @Test
    void save() {
        Permission permission = new Permission();
        given(permissionRepository.save(permission)).willReturn(permission);

        assertEquals(permission, permissionService.save(permission));
    }

    @Test
    void findById() {
        Permission permission = new Permission();
        given(permissionRepository.findById(MOCK_ID)).willReturn(Optional.of(permission));

        assertEquals(permission, permissionService.findById(MOCK_ID).get());
    }

    @Test
    void findAll() {
        List<Permission> permissionList = new ArrayList<>();
        given(permissionRepository.findAll()).willReturn(permissionList);

        assertEquals(permissionList, permissionService.findAll());
    }

    @Test
    void deleteById() {
        permissionService.deleteById(MOCK_ID);
        then(permissionRepository).should(times(1)).deleteById(MOCK_ID);
    }
}