package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;
import raf.si.racunovodstvo.knjizenje.services.TroskovniCentarService;
import raf.si.racunovodstvo.knjizenje.services.impl.ITroskovniCentarService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/troskovni-centri")
public class TroskovniCentarController {

    private final ITroskovniCentarService troskovniCentarService;

    public TroskovniCentarController(TroskovniCentarService troskovniCentarService) {
        this.troskovniCentarService = troskovniCentarService;
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTroskovniCentarById(@PathVariable("id") Long id){
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(id);
        if (optionalTroskovniCentar.isPresent()) {
            return ResponseEntity.ok(optionalTroskovniCentar.get());
        }
        throw new EntityNotFoundException();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTroskovniCentar(@Valid @RequestBody TroskovniCentar troskovniCentar){
        return ResponseEntity.ok(troskovniCentarService.save(troskovniCentar));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTroskovniCentar(@RequestBody TroskovniCentar troskovniCentar){
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(troskovniCentar.getId());
        if (optionalTroskovniCentar.isPresent()) {
            return ResponseEntity.ok(troskovniCentarService.updateTroskovniCentar(optionalTroskovniCentar.get()));
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTroskovniCentar(@PathVariable Long id) {
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(id);
        if (optionalTroskovniCentar.isPresent()) {
            troskovniCentarService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException();
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
            @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
            @RequestParam(value = "sort")  String[] sort
    ){
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page,size,sort);
        return ResponseEntity.ok(troskovniCentarService.findAll(pageSort));
    }

    @PutMapping(value = "/addFromKnjizenje",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addKontosFromKnjizenje(@RequestBody List<Konto> kontoList, @RequestBody TroskovniCentar troskovniCentar){
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(troskovniCentar.getId());
        if(optionalTroskovniCentar.isPresent()){
            return ResponseEntity.ok(troskovniCentarService.addKontosFromKnjizenje(kontoList,optionalTroskovniCentar.get()));
        }
        throw new EntityNotFoundException();
    }

}
