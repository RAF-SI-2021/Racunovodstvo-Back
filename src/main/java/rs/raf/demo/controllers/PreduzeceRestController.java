package rs.raf.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Preduzece;
import rs.raf.demo.services.IService;
import rs.raf.demo.services.impl.PreduzeceService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
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
        } else {
            return ResponseEntity.notFound().build();
        }
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
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePreduzece(@PathVariable("id") Long id){
        preduzeceService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
