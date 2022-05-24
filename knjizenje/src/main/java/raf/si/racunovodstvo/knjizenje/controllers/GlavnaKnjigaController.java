package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.services.KontoService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/glavna-knjiga")
public class GlavnaKnjigaController {

    private final KontoService kontoService;
    private final SearchUtil<Konto> searchUtil;

    public GlavnaKnjigaController(KontoService kontoService) {
        this.kontoService = kontoService;
        this.searchUtil = new SearchUtil<>();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPreduzeceById(
        @RequestParam(name = "search", required = false, defaultValue = "") String search,
        @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
        @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
        @RequestParam(defaultValue = "kontoId")  String[] sort
    ) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        Specification<Konto> spec = this.searchUtil.getSpec(search);
        return ResponseEntity.ok(this.kontoService.findAllGlavnaKnjigaResponseWithFilter(spec, pageSort));
    }

    @GetMapping(value = "/all",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.kontoService.findAllGlavnaKnjigaResponse());
    }
}
