package raf.si.racunovodstvo.nabavka.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;
import raf.si.racunovodstvo.nabavka.responses.ArtikalResponse;
import raf.si.racunovodstvo.nabavka.services.IArtikalService;
import raf.si.racunovodstvo.nabavka.utils.ApiUtil;
import raf.si.racunovodstvo.nabavka.utils.SearchUtil;
import raf.si.racunovodstvo.nabavka.validation.groups.OnCreate;
import raf.si.racunovodstvo.nabavka.validation.groups.OnUpdate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/artikli")
public class ArtikalController {

    private final IArtikalService iArtikalService;
    private final SearchUtil<Artikal> searchUtil;

    public ArtikalController(IArtikalService iArtikalService) {
        this.iArtikalService = iArtikalService;
        this.searchUtil = new SearchUtil<>();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ArtikalResponse>> findAll(
        @RequestParam(name = "search") String search,
        @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
        @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
        @RequestParam(defaultValue = "sifraArtikla") String[] sort
    ) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);

        if (search.isEmpty()) {
            return ResponseEntity.ok(this.iArtikalService.findAll(pageSort));
        }

        Specification<Artikal> spec = searchUtil.getSpec(search);
        Page<ArtikalResponse> result = iArtikalService.findAll(spec, pageSort);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{idKonverzijaKalkulacija}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ArtikalResponse>> findAllForKonverzijaOrKalkulacija(
        @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
        @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
        @RequestParam(defaultValue = "sifraArtikla") String[] sort,
        @PathVariable("idKonverzijaKalkulacija") Long idKonverzijaKalkulacija
    ) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        return ResponseEntity.ok(this.iArtikalService.findAllByIdKalkulacijaKonverzija(pageSort, idKonverzijaKalkulacija));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtikalResponse> create(@Validated(OnCreate.class) @RequestBody ArtikalRequest artikalRequest) {
        return ResponseEntity.ok(iArtikalService.save(artikalRequest));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtikalResponse> update(@Validated(OnUpdate.class) @RequestBody ArtikalRequest artikalRequest) {
        return ResponseEntity.ok(iArtikalService.update(artikalRequest));
    }

    @DeleteMapping(value = "/{artikalId}")
    public ResponseEntity<String> delete(@PathVariable("artikalId") Long artikalId) {
        iArtikalService.deleteById(artikalId);
        return ResponseEntity.noContent().build();
    }
}
