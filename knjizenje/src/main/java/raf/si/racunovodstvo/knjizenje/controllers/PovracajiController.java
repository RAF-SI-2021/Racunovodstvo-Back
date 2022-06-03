package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.Povracaj;
import raf.si.racunovodstvo.knjizenje.services.PovracajService;
import raf.si.racunovodstvo.knjizenje.services.impl.IPovracajService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@RequestMapping("/api/povracaji")
public class PovracajiController {

    private final IPovracajService povracajService;

    public PovracajiController(PovracajService povracajService) {
        this.povracajService = povracajService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPovracaji(
            @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
            @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
            @RequestParam(defaultValue = "povracajId")  String[] sort
    ) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        return ResponseEntity.ok(povracajService.findAll(pageSort).toList());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePovracaj(@PathVariable("id") Long id){
        Optional<Povracaj> optionalPovracaj = povracajService.findById(id);
        if (optionalPovracaj.isPresent()){
            povracajService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException();
    }
}
