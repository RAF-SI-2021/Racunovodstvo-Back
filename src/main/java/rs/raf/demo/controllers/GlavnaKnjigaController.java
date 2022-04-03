package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Konto;
import rs.raf.demo.responses.GlavnaKnjigaResponse;
import rs.raf.demo.services.impl.KontoService;
import rs.raf.demo.specifications.SpecificationsBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/glavna-knjiga")
public class GlavnaKnjigaController {

    private final KontoService kontoService;
    private final Pattern pattern;

    public GlavnaKnjigaController(KontoService kontoService) {
        this.kontoService = kontoService;
        this.pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
    }

    @GetMapping(value = "/{kontnaGrupa}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPreduzeceById(@PathVariable("kontnaGrupa") String kontnaGrupa,
                                              @RequestParam(name = "search", required = false, defaultValue = "") String search) {
        SpecificationsBuilder<Konto> builder = new SpecificationsBuilder<>();
        Matcher matcher = this.pattern.matcher(search + ",kontnaGrupa_brojKonta:" + kontnaGrupa + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Konto> spec = builder.build();

        try {
            List<Konto> kontoList = this.kontoService.findAll(spec);
            return ResponseEntity.ok(kontoList.stream().map(
                    konto -> new GlavnaKnjigaResponse(
                            konto.getKnjizenje().getKnjizenjeId(),
                            konto.getKnjizenje().getDatumKnjizenja(),
                            konto.getPotrazuje(),
                            konto.getDuguje(),
                            konto.getDuguje() - konto.getPotrazuje(),
                            konto.getKontnaGrupa().getNazivKonta(),
                            konto.getKontnaGrupa().getBrojKonta())
            ).collect(Collectors.toList()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
