package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;
import raf.si.racunovodstvo.knjizenje.services.KontnaGrupaService;
import raf.si.racunovodstvo.knjizenje.services.impl.IKontnaGrupaService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/konto")
public class KontnaGrupaController {

    private final IKontnaGrupaService kontnaGrupaService;

    public KontnaGrupaController(KontnaGrupaService kontnaGrupaService) {
        this.kontnaGrupaService = kontnaGrupaService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getKontnaGrupa(@PathVariable("id") Long id) {
        Optional<KontnaGrupa> optionalKontnaGrupa = kontnaGrupaService.findById(id);
        if (optionalKontnaGrupa.isPresent()) {
            return ResponseEntity.ok(optionalKontnaGrupa.get());
        }

        throw new EntityNotFoundException();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getKontneGrupe(@RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
                                            @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
                                            @RequestParam(defaultValue = "brojKonta", value = "sort") String[] sort) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        return ResponseEntity.ok(kontnaGrupaService.findAll(pageSort));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createKontnaGrupa(@Valid @RequestBody KontnaGrupa kontnaGrupa) {
        return ResponseEntity.ok(kontnaGrupaService.save(kontnaGrupa));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateKontnaGrupa(@Valid @RequestBody KontnaGrupa kontnaGrupa, @PathVariable Long id) {
        if (kontnaGrupaService.findById(id).isPresent())
            return ResponseEntity.ok(kontnaGrupaService.save(kontnaGrupa));
        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteKontnaGrupa(@PathVariable Long id) {
        Optional<KontnaGrupa> optionalKontnaGrupa = kontnaGrupaService.findById(id);
        if (optionalKontnaGrupa.isPresent()) {
            kontnaGrupaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new EntityNotFoundException();
    }
}
