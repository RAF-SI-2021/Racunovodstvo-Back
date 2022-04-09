package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Koeficijent;
import rs.raf.demo.services.IService;
import rs.raf.demo.services.impl.KoeficijentService;

import javax.persistence.EntityNotFoundException;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/koeficijenti")
public class KoeficijentController {
    private final IService<Koeficijent, Long> koeficijentService;

    public KoeficijentController(KoeficijentService koeficijentService) {
        this.koeficijentService = koeficijentService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(koeficijentService.findAll());
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody Koeficijent koeficijent) {
        if (koeficijentService.findById(koeficijent.getKoeficijentId()).isPresent())
            return ResponseEntity.ok(koeficijentService.save(koeficijent));
        throw new EntityNotFoundException();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Koeficijent koeficijent) {
        return ResponseEntity.ok(koeficijentService.save(koeficijent));
    }
}
