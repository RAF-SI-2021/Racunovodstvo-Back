package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.requests.TransakcijaRequest;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;
import raf.si.racunovodstvo.knjizenje.services.SifraTransakcijeService;
import raf.si.racunovodstvo.knjizenje.services.impl.ITransakcijaService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;
import raf.si.racunovodstvo.knjizenje.validation.groups.OnCreate;
import raf.si.racunovodstvo.knjizenje.validation.groups.OnUpdate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/transakcije")
public class TransakcijaController {

    private final ITransakcijaService transakcijaService;
    private final SearchUtil<Transakcija> searchUtil;

    public TransakcijaController(ITransakcijaService transakcijaService) {
        this.transakcijaService = transakcijaService;
        this.searchUtil = new SearchUtil<>();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TransakcijaResponse>> findAll(
        @RequestParam(defaultValue = "", required = false, name = "search") String search,
        @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
        @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
        @RequestParam(defaultValue = "brojTransakcije") String[] sort) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        return ResponseEntity.ok(Strings.isNotBlank(search) ? transakcijaService.search(searchUtil.getSpec(search), pageSort)
                                                            : transakcijaService.findAll(pageSort));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransakcijaResponse> create(@Validated(OnCreate.class) @RequestBody TransakcijaRequest transakcijaRequest) {
        return ResponseEntity.ok(transakcijaService.save(transakcijaRequest));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransakcijaResponse> update(@Validated(OnUpdate.class) @RequestBody TransakcijaRequest transakcijaRequest) {
        return ResponseEntity.ok(transakcijaService.update(transakcijaRequest));
    }

    @DeleteMapping(value = "/{transakcijaId}")
    public ResponseEntity<String> delete(@PathVariable("transakcijaId") Long transakcijaId) {
        transakcijaService.deleteById(transakcijaId);
        return ResponseEntity.noContent().build();
    }
}
