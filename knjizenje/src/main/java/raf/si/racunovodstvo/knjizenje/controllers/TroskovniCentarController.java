package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;
import raf.si.racunovodstvo.knjizenje.services.TroskovniCentarService;
import raf.si.racunovodstvo.knjizenje.services.impl.ITroskovniCentarService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/troskovni-centar")
public class TroskovniCentarController {

    private final ITroskovniCentarService troskovniCentarService;

    public TroskovniCentarController(TroskovniCentarService troskovniCentarService) {
        this.troskovniCentarService = troskovniCentarService;
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTroskovniCentarById(@PathVariable("id") Long id){
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(id);
        if (optionalTroskovniCentar.isPresent()) {
            return ResponseEntity.ok(optionalTroskovniCentar.get());
        }
        throw new EntityNotFoundException();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTroskovniCentar(@Valid @RequestBody TroskovniCentar troskovniCentar) throws IOException {
        return ResponseEntity.ok(troskovniCentarService.save(troskovniCentar));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addKontosFromKnjizenje(@Valid @RequestBody Knjizenje knjizenje, @PathVariable Long id){
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(id);
        if (optionalTroskovniCentar.isPresent()) {
            troskovniCentarService.addKontosIntoTroskovniCentar(knjizenje,optionalTroskovniCentar.get());
            return ResponseEntity.ok(troskovniCentarService.save(optionalTroskovniCentar.get()));
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTroskovniCentar(@PathVariable Long id) {
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(id);
        if (optionalTroskovniCentar.isPresent()) {
            troskovniCentarService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException();
    }
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(troskovniCentarService.findAll());
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateKontoInTroskovniCentar(@Valid @RequestBody Konto konto, @PathVariable Long id){
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(id);
        if (optionalTroskovniCentar.isPresent()) {
            troskovniCentarService.updateKontoInTroskovniCentar(konto,optionalTroskovniCentar.get());
            return ResponseEntity.ok(troskovniCentarService.save(optionalTroskovniCentar.get()));
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping (value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteKontoFromTroskovniCentar(@Valid @RequestBody Konto konto, @PathVariable Long id) {
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(id);
        if (optionalTroskovniCentar.isPresent()) {
            troskovniCentarService.deleteKontoFromTroskovniCentar(konto, optionalTroskovniCentar.get());
            return ResponseEntity.ok(troskovniCentarService.save(optionalTroskovniCentar.get()));
        }
        throw new EntityNotFoundException();
    }
}
