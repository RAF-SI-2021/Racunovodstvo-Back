package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Konto;
import rs.raf.demo.services.impl.KontoService;
import rs.raf.demo.utils.ApiUtil;
import rs.raf.demo.utils.SearchUtil;

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

    @GetMapping(value = "/{kontnaGrupa}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPreduzeceById(
            @PathVariable("kontnaGrupa") String kontnaGrupa,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
            @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
            @RequestParam(defaultValue = "kontoId")  String[] sort
            ) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        if (search.length() > 0) search += ",";
        Specification<Konto> spec = this.searchUtil.getSpec(search + "kontnaGrupa:" + kontnaGrupa + ",");
        return ResponseEntity.ok(this.kontoService.findAllGlavnaKnjigaResponse(spec, pageSort));
    }
}
