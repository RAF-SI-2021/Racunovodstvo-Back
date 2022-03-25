package rs.raf.demo.controllers;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.DnevnikKnjizenja;



import rs.raf.demo.services.IDnevnikKnjizenjaService;
import rs.raf.demo.services.impl.DnevnikKnjizenjaService;
import rs.raf.demo.specifications.CompareDnevnikKnjizenja;
import rs.raf.demo.specifications.RacunSpecificationsBuilder;


import javax.validation.Valid;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
@RequestMapping("/api/knjizenje")
public class KnjizenjeController {

    private final IDnevnikKnjizenjaService dnevnikKnjizenjaService;

    public KnjizenjeController(DnevnikKnjizenjaService dnevnikKnjizenjaService) {
        this.dnevnikKnjizenjaService = dnevnikKnjizenjaService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDnevnikKnjizenja(@Valid @RequestBody DnevnikKnjizenja dnevnikKnjizenja){
        return ResponseEntity.ok(dnevnikKnjizenjaService.save(dnevnikKnjizenja));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDnevnikKnjizenja(@Valid @RequestBody DnevnikKnjizenja dnevnikKnjizenja){
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestParam(name = "search") String search){
        RacunSpecificationsBuilder<DnevnikKnjizenja> builder = new RacunSpecificationsBuilder<>();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<DnevnikKnjizenja> spec = builder.build();

        try{
            List<DnevnikKnjizenja> result = dnevnikKnjizenjaService.findAll(spec);

            if(result.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Collections.sort(result, new CompareDnevnikKnjizenja());

            return ResponseEntity.ok(result);
        }
        catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}
