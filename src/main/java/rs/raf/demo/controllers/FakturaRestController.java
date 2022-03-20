package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Faktura;
import rs.raf.demo.services.FakturaService;

import java.util.List;

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

}
