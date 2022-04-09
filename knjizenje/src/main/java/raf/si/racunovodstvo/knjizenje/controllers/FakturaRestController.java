package raf.si.racunovodstvo.knjizenje.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.FakturaWithPreduzece;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.services.FakturaService;
import raf.si.racunovodstvo.knjizenje.services.impl.IFakturaService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@RequestMapping("/api/faktura")
public class FakturaRestController {

    private final IFakturaService fakturaService;

    private final SearchUtil<Faktura> searchUtil;

    private String preduzeceUrl = "http://localhost:8081/api/preduzece";

    public FakturaRestController(FakturaService fakturaService) {
        this.fakturaService = fakturaService;
        this.searchUtil = new SearchUtil<>();
    }
    private Preduzece getPreduzeceById(long id) throws IOException {

        URL url = new URL(preduzeceUrl + "/" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setReadTimeout(5000);
        con.connect();

        int code = con.getResponseCode();

        if(code == 404){
            return null;
        }

        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        String result = content.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result, Preduzece.class);
    }

    private FakturaWithPreduzece addPreduzeceToFaktura(Faktura faktura) throws IOException {

        FakturaWithPreduzece fakturaWithPreduzece = new FakturaWithPreduzece(faktura,getPreduzeceById(faktura.getPreduzeceId()));
        return fakturaWithPreduzece;
    }

    private List<FakturaWithPreduzece> addPreduzeceToFakturaList(List<Faktura> sveFakture) throws IOException {

        List<FakturaWithPreduzece> sveFaktureWithPreduzece = new ArrayList<>();
        for(Faktura faktura:sveFakture){
            sveFaktureWithPreduzece.add(addPreduzeceToFaktura(faktura));
        }
        return sveFaktureWithPreduzece;
    }

    @GetMapping(value = "/sume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSume(@RequestParam String tipFakture){
        return ResponseEntity.ok(fakturaService.getSume(tipFakture));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFakture(
            @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
            @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
            @RequestParam(defaultValue = "dokumentId")  String[] sort
    ) throws IOException {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        return ResponseEntity.ok(addPreduzeceToFakturaList(fakturaService.findAll(pageSort).toList()));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestParam(name = "search") String search) throws IOException {
        Specification<Faktura> spec = this.searchUtil.getSpec(search);

        List<Faktura> result = fakturaService.findAll(spec);

        return ResponseEntity.ok(addPreduzeceToFakturaList(result));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFaktura(@Valid @RequestBody Faktura faktura) throws IOException {

        if(getPreduzeceById(faktura.getPreduzeceId())== null){
            throw new PersistenceException(String.format("Ne postoji preduzece sa id-jem %s",faktura.getPreduzeceId()));
        }
        return ResponseEntity.ok(fakturaService.save(faktura));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFaktura(@Valid @RequestBody Faktura faktura) throws IOException {
        Long preduzeceId = faktura.getPreduzeceId();
        if(preduzeceId != null && getPreduzeceById(faktura.getPreduzeceId()) == null){
            throw new PersistenceException(String.format("Ne postoji preduzece sa id-jem %s",faktura.getPreduzeceId()));
        }

        Optional<Faktura> optionalFaktura = fakturaService.findById(faktura.getDokumentId());
        if(optionalFaktura.isPresent()) {
            return ResponseEntity.ok(fakturaService.save(faktura));
        }

        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFaktura(@PathVariable("id") Long id){
        Optional<Faktura> optionalFaktura = fakturaService.findById(id);
        if (optionalFaktura.isPresent()){
            fakturaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new EntityNotFoundException();
    }
}
