package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Faktura;
import rs.raf.demo.services.FakturaService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/faktura")
public class FakturaRestController {

    private final FakturaService fakturaService;

    public FakturaRestController(FakturaService fakturaService) {
        this.fakturaService = fakturaService;
    }

    @GetMapping(value = "/ulazneFakture", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUlazneFakture() {
        List<Faktura> ulazneFakture = fakturaService.findUlazneFakture();
        if(ulazneFakture.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(ulazneFakture);
        }
    }

    @GetMapping(value = "/izlazneFakture", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIzlazneFakture() {
        List<Faktura> izlazneFakture = fakturaService.findIzlacneFakture();
        if(izlazneFakture.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(izlazneFakture);
        }
    }

    @GetMapping(value = "/fakture", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFakture(){
        if(fakturaService.findAll().isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(fakturaService.findAll());
        }
    }

    /*@PostMapping(value = "/create/{fakturaId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> novaFaktura(@RequestBody Faktura faktura){

    }

    @PutMapping(value = "/edit/{fakturaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> novaFaktura(@PathVariable Long fakturaId){
        Optional<Faktura> optionalFaktura = fakturaService.findById(fakturaId);

        if(optionalFaktura.isPresent()){
            return ResponseEntity.ok(200);
        }else{
            return ResponseEntity.status(404).build();
        }

    }
    @DeleteMapping(path = "/detleteFaktura/{fakturaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFaktura(@PathVariable Long fakturaId){

    }*/
}
