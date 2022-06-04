package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.requests.BazniCentarRequest;
import raf.si.racunovodstvo.knjizenje.services.ProfitniCentarService;
import raf.si.racunovodstvo.knjizenje.services.impl.IProfitniCentarService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/profitni-centri")
public class ProfitniCentarController {
    private final IProfitniCentarService profitniCentarService;

    public ProfitniCentarController(ProfitniCentarService profitniCentarService) {
        this.profitniCentarService = profitniCentarService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProfitniCentarById(@PathVariable("id") Long id){
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(id);
        if (optionalProfitniCentar.isPresent()) {
            return ResponseEntity.ok(optionalProfitniCentar.get());
        }
        throw new EntityNotFoundException();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProfitniCentar(@Valid @RequestBody ProfitniCentar profitniCentar) {
        return ResponseEntity.ok(profitniCentarService.save(profitniCentar));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfitniCentar(@RequestBody ProfitniCentar profitniCentar){
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(profitniCentar.getId());
        if (optionalProfitniCentar.isPresent()) {
            System.out.println(profitniCentar+"_-------------------------------------- Controller succes\n");
            System.out.println(optionalProfitniCentar.get().getId());
            return ResponseEntity.ok(profitniCentarService.updateProfitniCentar(profitniCentar));
        }
        System.out.println(profitniCentar+"_-------------------------------------- Controller fail\n");
        System.out.println(optionalProfitniCentar.get().getId());
        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProfitniCentar(@PathVariable Long id) {
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(id);
        if (optionalProfitniCentar.isPresent()) {
            profitniCentarService.deleteById(id);
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
        return ResponseEntity.ok(profitniCentarService.findAll(pageSort));
    }

    @PutMapping(value = "/addFromKnjizenje",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addKontosFromKnjizenje(@RequestBody BazniCentarRequest bazniCentarRequest){
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(bazniCentarRequest.getBazniCentar().getId());
        if(optionalProfitniCentar.isPresent()){
            return ResponseEntity.ok(profitniCentarService.addKontosFromKnjizenje(bazniCentarRequest.getKnjizenje(),optionalProfitniCentar.get()));
        }
        throw new EntityNotFoundException();
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(profitniCentarService.findAllProfitniCentarResponse());
    }

}
