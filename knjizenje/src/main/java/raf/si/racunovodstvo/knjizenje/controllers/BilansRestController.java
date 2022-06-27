package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IBilansService;

import java.util.*;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/bilans")
public class BilansRestController {

    private final IBilansService bilansService;

    public BilansRestController(IBilansService bilansService) {
        this.bilansService = bilansService;
    }

    @GetMapping(value = "/bilansStanja", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBilansStanja(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiOd,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiDo) {
        Map<String,List<BilansResponse>> response = bilansService.findBilans(true, datumiOd, datumiDo);
        return response.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(response);
    }

    @GetMapping(value = "/bilansUspeha", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBilansUspeha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiOd,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiDo) {
        Map<String,List<BilansResponse>> response = bilansService.findBilans(false, datumiOd, datumiDo);
        return response.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(response);
    }

    @GetMapping(value = "/brutoBilans", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBrutoBilans(@RequestParam String brojKontaOd,
                                            @RequestParam String brojKontaDo,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datumOd,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datumDo) {
        List<BilansResponse> response = bilansService.findBrutoBilans(brojKontaOd, brojKontaDo, datumOd, datumDo);
        return response.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(response);
    }
}
