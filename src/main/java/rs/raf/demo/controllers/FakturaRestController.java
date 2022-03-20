package rs.raf.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.services.FakturaService;

@CrossOrigin
@RestController
@RequestMapping("/api/faktura")
public class FakturaRestController {

    private final FakturaService fakturaService;

    public FakturaRestController(FakturaService fakturaService) {
        this.fakturaService = fakturaService;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFaktura(@PathVariable("id") Long id){
        fakturaService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}