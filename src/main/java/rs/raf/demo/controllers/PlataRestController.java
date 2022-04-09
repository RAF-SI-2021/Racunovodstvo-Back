package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Plata;
import rs.raf.demo.requests.PlataRequest;
import rs.raf.demo.services.impl.PlataService;
import rs.raf.demo.utils.SearchUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api")
public class PlataRestController {
    private final PlataService plataService;
    private final SearchUtil<Plata> searchUtil;

    public PlataRestController(PlataService plataService) {
        this.plataService = plataService;
        this.searchUtil = new SearchUtil<>();
    }

    @GetMapping(value = "/zaposleni/{id}/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlataForZaposleni(@PathVariable("id") Long zaposleniId) {
        return ResponseEntity.ok(this.plataService.findByZaposleniZaposleniId(zaposleniId));
    }

    @GetMapping(value = "/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlata(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        Specification<Plata> spec = this.searchUtil.getSpec(search);
        return ResponseEntity.ok(this.plataService.findAll(spec));
    }

    @GetMapping(value = "/plata/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPlata() {
        return ResponseEntity.ok(this.plataService.findAll());
    }

    @GetMapping(value = "/plata/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlataById(@PathVariable("id") Long plataId) {
        Optional<Plata> optionalPlata = this.plataService.findById(plataId);
        if (optionalPlata.isPresent())
            return ResponseEntity.ok(optionalPlata.get());
        throw new EntityNotFoundException();
    }

    @PostMapping(value = "/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newPlata(@Valid @RequestBody PlataRequest plata) {
        try {
            return ResponseEntity.ok(this.plataService.save(plata));
        } catch (Exception e) {
            throw new EntityNotFoundException();
        }
    }

    @PutMapping(value = "/plata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editPlata(@Valid @RequestBody PlataRequest plata) {
        Optional<Plata> optionalPlata = this.plataService.findById(plata.getPlataId());
        if (optionalPlata.isPresent()) {
            try {
                return ResponseEntity.ok(this.plataService.save(plata));
            } catch (Exception e) {
                throw new EntityNotFoundException();
            }
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
