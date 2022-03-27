package rs.raf.demo.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.services.impl.PermissionService;

@CrossOrigin
@RestController
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
