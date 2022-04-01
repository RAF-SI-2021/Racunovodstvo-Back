package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.KontnaGrupa;
import rs.raf.demo.services.IKontnaGrupaService;
import rs.raf.demo.services.impl.KontnaGrupaService;
import rs.raf.demo.utils.ApiUtil;

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
    public ResponseEntity<?> getKontnaGrupa(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(kontnaGrupaService.findKontnaGrupaById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
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

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateKontnaGrupa(@Valid @RequestBody KontnaGrupa kontnaGrupa) {
        return ResponseEntity.ok(kontnaGrupaService.update(kontnaGrupa.getBrojKonta()));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteKontnaGrupa(@PathVariable String id) {
        kontnaGrupaService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
