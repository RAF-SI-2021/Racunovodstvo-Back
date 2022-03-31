package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Zaposleni;
import rs.raf.demo.services.IZaposleniService;
import rs.raf.demo.specifications.RacunSpecificationsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/zaposleni")
public class ZaposleniRestController {

    private final IZaposleniService iZaposleniService;

    public ZaposleniRestController(IZaposleniService iZaposleniService) {
        this.iZaposleniService = iZaposleniService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createZaposleni(@Valid @RequestBody Zaposleni zaposleni) {
        return ResponseEntity.ok(iZaposleniService.save(zaposleni));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateZaposleni(@Valid @RequestBody Zaposleni zaposleni) {
        Optional<Zaposleni> optionalZaposleni = iZaposleniService.findById(zaposleni.getZaposleniId());
        if (optionalZaposleni.isPresent()) {
            return ResponseEntity.ok(iZaposleniService.save(zaposleni));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteZaposleni(@PathVariable("id") Long id) {
        Optional<Zaposleni> optionalZaposleni = iZaposleniService.findById(id);
        if (optionalZaposleni.isPresent()) {
            iZaposleniService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getZaposleniId(@PathVariable("id") Long id) {
        Optional<Zaposleni> optionalZaposleni = iZaposleniService.findById(id);
        if (optionalZaposleni.isPresent()) {
            return ResponseEntity.ok(iZaposleniService.findById(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestParam(name = "search") String search) {
        RacunSpecificationsBuilder<Zaposleni> builder = new RacunSpecificationsBuilder<>();

        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Zaposleni> spec = builder.build();

        List<Zaposleni> result = iZaposleniService.findAll(spec);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}
