package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Faktura;
import rs.raf.demo.services.IFakturaService;
import rs.raf.demo.services.impl.FakturaService;

import rs.raf.demo.utils.ApiUtil;

import rs.raf.demo.specifications.RacunSpecificationsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Optional;
import java.util.Map;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@RequestMapping("/api/faktura")
public class FakturaRestController {

    private final IFakturaService fakturaService;

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
        List<Faktura> izlazneFakture = fakturaService.findIzlazneFakture();
        if(izlazneFakture.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(izlazneFakture);
        }
    }

    @GetMapping(value = "/sume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSume(@RequestParam String tipFakture){
        Map<String, Double> sume = fakturaService.getSume(tipFakture);
        if(!sume.isEmpty()) {
            return ResponseEntity.ok(sume);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFakture(
            @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
            @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
            @RequestParam(defaultValue = "dokumentId")  String[] sort
    ) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        return ResponseEntity.ok(fakturaService.findAll(pageSort));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestParam(name = "search") String search){
        RacunSpecificationsBuilder<Faktura> builder = new RacunSpecificationsBuilder<>();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Faktura> spec = builder.build();

        try{
            List<Faktura> result = fakturaService.findAll(spec);

            if(result.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(result);
        }
        catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFaktura(@Valid @RequestBody Faktura faktura){
        return ResponseEntity.ok(fakturaService.save(faktura));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFaktura(@Valid @RequestBody Faktura faktura){
        Optional<Faktura> optionalFaktura = fakturaService.findById(faktura.getDokumentId());
        if(optionalFaktura.isPresent()) {
            return ResponseEntity.ok(fakturaService.save(faktura));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFaktura(@PathVariable("id") Long id){
        fakturaService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
