package raf.si.racunovodstvo.user.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.user.services.impl.PermissionService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PermissionRestControllerTest {

    @InjectMocks
    private PermissionRestController permissionRestController;
    @Mock
    private PermissionService permissionService;

    @Test
    void getAllPermissions() {
        given(permissionService.findAll()).willReturn(new ArrayList<>());
        ResponseEntity<?> responseEntity = permissionRestController.getAllPermissions();

        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
