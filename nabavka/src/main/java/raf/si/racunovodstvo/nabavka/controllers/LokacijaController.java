package raf.si.racunovodstvo.nabavka.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.nabavka.model.Lokacija;
import raf.si.racunovodstvo.nabavka.services.impl.LokacijaService;
import javax.validation.Valid;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@RequestMapping("/api/lokacije")
public class LokacijaController {

    private final LokacijaService lokacijaService;

    public LokacijaController(LokacijaService lokacijaService){
        this.lokacijaService = lokacijaService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createLokacija(@Valid @RequestBody Lokacija lokacija) {
        return ResponseEntity.ok(lokacijaService.save(lokacija));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLokacija(@Valid @RequestBody Lokacija lokacija) {
        return ResponseEntity.ok(lokacijaService.update(lokacija));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLokacije() {
        return ResponseEntity.ok(lokacijaService.findAll());
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteLokacija(@PathVariable Long id) {
        lokacijaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
