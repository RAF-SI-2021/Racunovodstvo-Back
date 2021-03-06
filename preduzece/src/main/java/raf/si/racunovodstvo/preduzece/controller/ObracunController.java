package raf.si.racunovodstvo.preduzece.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.preduzece.model.Obracun;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaradeRequest;
import raf.si.racunovodstvo.preduzece.services.impl.ObracunService;

import javax.validation.Valid;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/obracun")
public class ObracunController {
    private final ObracunService obracunService;

    public ObracunController(ObracunService obracunService) {
        this.obracunService = obracunService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllObracunWithZaposleni() {
        return ResponseEntity.ok(obracunService.findAll());
    }

    @PutMapping
    public ResponseEntity<?> setObracunZaradeNaziv(@Valid @RequestBody ObracunZaradeRequest obracunZaradeRequest) {
        obracunService.updateObracunZaradeNaziv(obracunZaradeRequest.getObracunZaradeId(), obracunZaradeRequest.getNaziv());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/obradi/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> obradiObracun(@PathVariable("id") Long id, @RequestHeader(name="Authorization") String token) {
        Obracun obracun = obracunService.obradiObracun(id,token);
        return ResponseEntity.ok(obracunService.findAll());
    }

}
