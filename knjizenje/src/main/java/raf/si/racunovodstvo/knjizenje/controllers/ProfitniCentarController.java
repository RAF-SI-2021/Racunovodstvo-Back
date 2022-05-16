package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.services.ProfitniCentarService;
import raf.si.racunovodstvo.knjizenje.services.impl.IProfitniCentarService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/profitni-centar")
public class ProfitniCentarController {
    private final IProfitniCentarService profitniCentarService;

    public ProfitniCentarController(ProfitniCentarService profitniCentarService) {
        this.profitniCentarService = profitniCentarService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProfitniCentarById(@PathVariable("id") Long id){
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(id);
        if (optionalProfitniCentar.isPresent()) {
            return ResponseEntity.ok(optionalProfitniCentar.get());
        }
        throw new EntityNotFoundException();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProfitniCentar(@Valid @RequestBody ProfitniCentar profitniCentar) throws IOException {
        return ResponseEntity.ok(profitniCentarService.save(profitniCentar));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addKontosFromKnjizenje(@Valid @RequestBody Knjizenje knjizenje, @PathVariable Long id){
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(id);
        if (optionalProfitniCentar.isPresent()) {
            profitniCentarService.addKontosIntoProfitniCentar(knjizenje,optionalProfitniCentar.get());
            return ResponseEntity.ok(profitniCentarService.save(optionalProfitniCentar.get()));
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProfitniCentar(@PathVariable Long id) {
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(id);
        if (optionalProfitniCentar.isPresent()) {
            profitniCentarService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException();
    }
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(profitniCentarService.findAll());
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateKontoInProfitniCentar(@Valid @RequestBody Konto konto, @PathVariable Long id){
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(id);
        if (optionalProfitniCentar.isPresent()) {
            profitniCentarService.updateKontoInProfitniCentar(konto,optionalProfitniCentar.get());
            return ResponseEntity.ok(profitniCentarService.save(optionalProfitniCentar.get()));
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping (value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteKontoFromProfitniCentar(@Valid @RequestBody Konto konto, @PathVariable Long id) {
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(id);
        if (optionalProfitniCentar.isPresent()) {
            profitniCentarService.deleteKontoFromProfitniCentar(konto, optionalProfitniCentar.get());
            return ResponseEntity.ok(profitniCentarService.save(optionalProfitniCentar.get()));
        }
        throw new EntityNotFoundException();
    }

}
