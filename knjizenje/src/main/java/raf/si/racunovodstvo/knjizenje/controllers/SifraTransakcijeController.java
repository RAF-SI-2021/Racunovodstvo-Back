package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.requests.SifraTransakcijeRequest;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.ISifraTransakcijeService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;
import raf.si.racunovodstvo.knjizenje.validation.groups.OnCreate;
import raf.si.racunovodstvo.knjizenje.validation.groups.OnUpdate;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/sifraTransakcije")
public class SifraTransakcijeController {

    private final ISifraTransakcijeService sifraTransakcijeService;
    private final SearchUtil<SifraTransakcije> searchUtil;

    public SifraTransakcijeController(ISifraTransakcijeService sifraTransakcijeService) {
        this.sifraTransakcijeService = sifraTransakcijeService;
        this.searchUtil = new SearchUtil<>();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SifraTransakcijeResponse>> findAll(
            @RequestParam(defaultValue = "", required = false, name = "search") String search,
            @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
            @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
            @RequestParam(defaultValue = "nazivTransakcije") String[] sort,
            @RequestHeader("Authorization") String token) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        return ResponseEntity.ok(Strings.isNotBlank(search) ? sifraTransakcijeService.search(searchUtil.getSpec(search), pageSort, token)
                : sifraTransakcijeService.findAll(pageSort, token));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SifraTransakcijeResponse> create(@Validated(OnCreate.class) @RequestBody SifraTransakcijeRequest sifraTransakcijeRequest) {
        return ResponseEntity.ok(sifraTransakcijeService.save(sifraTransakcijeRequest));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SifraTransakcijeResponse> update(@Validated(OnUpdate.class) @RequestBody SifraTransakcijeRequest sifraTransakcijeRequest) {
        return ResponseEntity.ok(sifraTransakcijeService.update(sifraTransakcijeRequest));
    }

    @DeleteMapping(value = "/{sifraTransakcijeId}")
    public ResponseEntity<String> delete(@PathVariable("sifraTransakcijeId") Long sifraTransakcijeId) {
        sifraTransakcijeService.deleteById(sifraTransakcijeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<SifraTransakcije> optionalSifraTransakcije = sifraTransakcijeService.findById(id);
        if (optionalSifraTransakcije.isPresent()) {
            return ResponseEntity.ok(optionalSifraTransakcije.get());
        }

        throw new EntityNotFoundException();
    }
}
