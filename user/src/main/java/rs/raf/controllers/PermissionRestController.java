package rs.raf.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.services.impl.PermissionService;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/permissions")
public class PermissionRestController {

    private final PermissionService permissionService;

    public PermissionRestController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPermissions(){
        return ResponseEntity.ok(permissionService.findAll());
    }

}
