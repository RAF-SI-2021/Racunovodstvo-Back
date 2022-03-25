package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.DnevnikKnjizenja;

import rs.raf.demo.services.impl.DnevnikKnjizenjaService;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/knjizenje")
public class KnjizenjeController {

    private final DnevnikKnjizenjaService dnevnikKnjizenjaService;

    public KnjizenjeController(DnevnikKnjizenjaService dnevnikKnjizenjaService) {
        this.dnevnikKnjizenjaService = dnevnikKnjizenjaService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDnevnikKnjizenja(@Valid @RequestBody DnevnikKnjizenja dnevnikKnjizenja){
        return ResponseEntity.ok(dnevnikKnjizenjaService.save(dnevnikKnjizenja));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDnevnikKnjizenja(@Valid @RequestBody DnevnikKnjizenja dnevnikKnjizenja){ // jos jedna uslov ako nije validan????
        Optional<DnevnikKnjizenja> optionalDnevnik = dnevnikKnjizenjaService.findById(dnevnikKnjizenja.getDnevnikKnjizenjaId());
        if(optionalDnevnik.isPresent()) {
            return ResponseEntity.ok(dnevnikKnjizenjaService.save(dnevnikKnjizenja));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteDnevnikKnjizenja(@PathVariable("id") Long id){
        Optional<DnevnikKnjizenja> optionalDnevnik = dnevnikKnjizenjaService.findById(id);
        if(optionalDnevnik.isPresent()) {
            dnevnikKnjizenjaService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getDnevnikKnjizenjaId(@PathVariable("id") Long id){
        Optional<DnevnikKnjizenja> optionalDnevnik = dnevnikKnjizenjaService.findById(id);
        if(optionalDnevnik.isPresent()) {
            return ResponseEntity.ok(dnevnikKnjizenjaService.findById(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    

}
