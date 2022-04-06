package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Plata;
import rs.raf.demo.services.impl.PlataService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api")
public class PlataRestController {
    private final PlataService plataService;

    public PlataRestController(PlataService plataService) {
        this.plataService = plataService;
    }

    @GetMapping(value = "/zaposleni/{id}/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlataForZaposleni(@PathVariable("id") Long zaposleniId) {
        return ResponseEntity.ok(this.plataService.findByZaposleniZaposleniId(zaposleniId));
    }

    @GetMapping(value = "/plata/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlataById(@PathVariable("id") Long plataId) {
        Optional<Plata> optionalPlata = this.plataService.findById(plataId);
        if (optionalPlata.isPresent())
            return ResponseEntity.ok(optionalPlata.get());
        throw new EntityNotFoundException();
    }

    @PostMapping(value = "/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newPlata(@Valid @RequestBody Plata plata) {
        return ResponseEntity.ok(this.plataService.save(plata));
    }

    @PutMapping(value = "/plata/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newPlata(@PathVariable("id") Long plataId, @Valid @RequestBody Plata plata) {
        Optional<Plata> optionalPlata = this.plataService.findById(plataId);
        if (optionalPlata.isPresent()) {
            plata.setPlataId(plataId);
            return ResponseEntity.ok(this.plataService.save(plata));
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/plata/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePlata(@PathVariable("id") Long plataId) {
        Optional<Plata> optionalPlata = this.plataService.findById(plataId);
        if (optionalPlata.isPresent()) {
            this.plataService.deleteById(plataId);
            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException();
    }
}