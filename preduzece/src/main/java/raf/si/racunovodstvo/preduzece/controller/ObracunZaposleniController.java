package raf.si.racunovodstvo.preduzece.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.services.IObracunZaposleniService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/obracunZaposleni")
public class ObracunZaposleniController {

    private IObracunZaposleniService iObracunZaposleniService;

    private ObracunZaposleniController(IObracunZaposleniService iObracunZaposleniService){
        this.iObracunZaposleniService = iObracunZaposleniService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createObracunZaposleni(@RequestParam Long zaposleniId, @RequestParam @Min(0) @Max(1) Double ucinak, @RequestParam Long obracunId) {
        return ResponseEntity.ok(iObracunZaposleniService.save(zaposleniId, ucinak, obracunId));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteObracunZaposleni(@PathVariable("id") Long id){
        Optional<ObracunZaposleni> optionalObracunZaposleni = iObracunZaposleniService.findById(id);

        if (optionalObracunZaposleni.isPresent()) {
            iObracunZaposleniService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException();
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllObracunZaposleni() {
        return ResponseEntity.ok(iObracunZaposleniService.findAll());
    }


}
