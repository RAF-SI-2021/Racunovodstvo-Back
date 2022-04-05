package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Preduzece;
import rs.raf.demo.services.IService;
import rs.raf.demo.services.impl.PreduzeceService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            return ResponseEntity.noContent().build();
        }

        throw new EntityNotFoundException();
    }
}
