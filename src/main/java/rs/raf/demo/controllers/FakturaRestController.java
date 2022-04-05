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

import rs.raf.demo.utils.SearchUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@RequestMapping("/api/faktura")
public class FakturaRestController {

    private final IFakturaService fakturaService;

    private final SearchUtil<Faktura> searchUtil;

    public FakturaRestController(FakturaService fakturaService) {
        this.fakturaService = fakturaService;
        this.searchUtil = new SearchUtil<>();
    }

    @GetMapping(value = "/sume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSume(@RequestParam String tipFakture){
        return ResponseEntity.ok(fakturaService.getSume(tipFakture));
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
        Specification<Faktura> spec = this.searchUtil.getSpec(search);

        List<Faktura> result = fakturaService.findAll(spec);

        return ResponseEntity.ok(result);
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
        }

        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFaktura(@PathVariable("id") Long id){
        Optional<Faktura> optionalFaktura = fakturaService.findById(id);
        if (optionalFaktura.isPresent()){
            fakturaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new EntityNotFoundException();
    }
}
