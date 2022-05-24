package raf.si.racunovodstvo.preduzece.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.si.racunovodstvo.preduzece.model.Preduzece;
import raf.si.racunovodstvo.preduzece.services.IService;
import raf.si.racunovodstvo.preduzece.services.impl.PreduzeceService;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/preduzece")
public class PreduzeceRestController {

    private final IService<Preduzece, Long> preduzeceService;

    public PreduzeceRestController(PreduzeceService preduzeceService){
        this.preduzeceService = preduzeceService;
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPreduzece() {
        return ResponseEntity.ok(preduzeceService.findAll());
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPreduzeceById(@PathVariable("id") Long id){
        Optional<Preduzece> optionalPreduzece = preduzeceService.findById(id);
        if(optionalPreduzece.isPresent()) {
            return ResponseEntity.ok(optionalPreduzece.get());
        }

        throw new EntityNotFoundException();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPreduzece(@Valid @RequestBody Preduzece preduzece){
        return ResponseEntity.ok(preduzeceService.save(preduzece));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePreduzece(@Valid @RequestBody Preduzece preduzece){
        Optional<Preduzece> optionalPreduzece = preduzeceService.findById(preduzece.getPreduzeceId());
        if(optionalPreduzece.isPresent()) {
            return ResponseEntity.ok(preduzeceService.save(preduzece));
        }

        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePreduzece(@PathVariable("id") Long id){
        Optional<Preduzece> optionalPreduzece = preduzeceService.findById(id);

        if (optionalPreduzece.isPresent()) {
            preduzeceService.deleteById(id);
            return ResponseEntity.ok().build();
        }

        throw new EntityNotFoundException();
    }
}
