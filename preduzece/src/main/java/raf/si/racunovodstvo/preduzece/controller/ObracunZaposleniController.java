package raf.si.racunovodstvo.preduzece.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaposleniRequest;
import raf.si.racunovodstvo.preduzece.services.IObracunZaposleniService;
import raf.si.racunovodstvo.preduzece.utils.ApiUtil;
import raf.si.racunovodstvo.preduzece.utils.SearchUtil;
import raf.si.racunovodstvo.preduzece.validation.groups.OnCreate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/obracun_zaposleni")
public class ObracunZaposleniController {

    private final IObracunZaposleniService iObracunZaposleniService;
    private final SearchUtil<ObracunZaposleni> searchUtil;

    public ObracunZaposleniController(IObracunZaposleniService iObracunZaposleniService){
        this.iObracunZaposleniService = iObracunZaposleniService;
        this.searchUtil = new SearchUtil<>();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createObracunZaposleni(@Validated(OnCreate.class) @RequestBody ObracunZaposleniRequest obracunZaposleniRequest) {
        return ResponseEntity.ok(iObracunZaposleniService.save(obracunZaposleniRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteObracunZaposleni(@PathVariable("id") Long id){
        iObracunZaposleniService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllObracunZaposleni() {
        return ResponseEntity.ok(iObracunZaposleniService.findAll());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestParam(name = "search", defaultValue = "") String search,
                                    @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
                                    @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
                                    @RequestParam(defaultValue = "-ucinak") String[] sort) {

        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        if(StringUtils.isBlank(search)){
            return ResponseEntity.ok(iObracunZaposleniService.findAll(pageSort));
        }

        Specification<ObracunZaposleni> spec = searchUtil.getSpec(search);
        return ResponseEntity.ok(iObracunZaposleniService.findAll(spec, pageSort));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestParam(required = false) Double ucinak,
                                    @RequestParam(required = false) Double netoPlata,
                                    @RequestParam Long idObracunZaposleni){
        return ResponseEntity.ok(iObracunZaposleniService.update(ucinak, netoPlata, idObracunZaposleni));
    }
}
