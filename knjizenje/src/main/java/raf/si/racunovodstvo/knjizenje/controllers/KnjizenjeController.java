package raf.si.racunovodstvo.knjizenje.controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.ProfitniCentar;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;
import raf.si.racunovodstvo.knjizenje.responses.KnjizenjeResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IKnjizenjeService;
import raf.si.racunovodstvo.knjizenje.services.impl.IProfitniCentarService;
import raf.si.racunovodstvo.knjizenje.services.impl.ITroskovniCentarService;
import raf.si.racunovodstvo.knjizenje.specifications.RacunSpecificationsBuilder;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/knjizenje")
public class KnjizenjeController {


    private final IKnjizenjeService knjizenjaService;
    private final IProfitniCentarService profitniCentarService;
    private final ITroskovniCentarService troskovniCentarService;
    private final SearchUtil<Knjizenje> searchUtil;


    public KnjizenjeController(IKnjizenjeService knjizenjaService, IProfitniCentarService profitniCentarService, ITroskovniCentarService troskovniCentarService) {
        this.knjizenjaService = knjizenjaService;
        this.profitniCentarService = profitniCentarService;
        this.troskovniCentarService = troskovniCentarService;
        this.searchUtil = new SearchUtil<>();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDnevnikKnjizenja(@Valid @RequestBody Knjizenje dnevnikKnjizenja, @RequestParam Long bazniCentarId) {
        Optional<TroskovniCentar> optionalTroskovniCentar = troskovniCentarService.findById(bazniCentarId);
        Optional<ProfitniCentar> optionalProfitniCentar = profitniCentarService.findById(bazniCentarId);
        if(optionalTroskovniCentar.isPresent()){
            troskovniCentarService.addKontosFromKnjizenje(dnevnikKnjizenja ,optionalTroskovniCentar.get());
        }else if(optionalProfitniCentar.isPresent()){
            profitniCentarService.addKontosFromKnjizenje(dnevnikKnjizenja,optionalProfitniCentar.get());
        }
        return ResponseEntity.ok(knjizenjaService.save(dnevnikKnjizenja));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDnevnikKnjizenja(@Valid @RequestBody Knjizenje dnevnikKnjizenja) {
        Optional<Knjizenje> optionalDnevnik = knjizenjaService.findById(dnevnikKnjizenja.getKnjizenjeId());
        if (optionalDnevnik.isPresent()) {
            return ResponseEntity.ok(knjizenjaService.save(dnevnikKnjizenja));
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteDnevnikKnjizenja(@PathVariable("id") Long id) {
        Optional<Knjizenje> optionalDnevnik = knjizenjaService.findById(id);
        if (optionalDnevnik.isPresent()) {
            knjizenjaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getDnevnikKnjizenjaId(@PathVariable("id") Long id) {
        Optional<Knjizenje> optionalDnevnik = knjizenjaService.findById(id);
        if (optionalDnevnik.isPresent()) {
            return ResponseEntity.ok(optionalDnevnik.get());
        }
        throw new EntityNotFoundException();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestParam(name = "search") String search,
                                    @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
                                    @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
                                    @RequestParam(defaultValue = "-datumKnjizenja") String[] sort) {
        RacunSpecificationsBuilder<Knjizenje> builder = new RacunSpecificationsBuilder<>();
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);

        Specification<Knjizenje> spec = searchUtil.getSpec(search);
        Page<KnjizenjeResponse> result = knjizenjaService.findAll(spec, pageSort);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(knjizenjaService.findAllKnjizenjeResponse());
    }

    @GetMapping(value = "/{id}/kontos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getKontoByKnjizenjeId(@PathVariable Long knjizenjeId){
        return ResponseEntity.ok(knjizenjaService.findKontoByKnjizenjeId(knjizenjeId));
    }
}
