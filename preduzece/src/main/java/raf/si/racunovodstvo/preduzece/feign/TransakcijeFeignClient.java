package raf.si.racunovodstvo.preduzece.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.preduzece.model.Transakcija;
import raf.si.racunovodstvo.preduzece.requests.ObracunTransakcijeRequest;
import raf.si.racunovodstvo.preduzece.responses.SifraTransakcijeResponse;

import java.util.List;

@FeignClient(value = "knjizenje/api")
public interface TransakcijeFeignClient {

    @PostMapping("/transakcije")
    ResponseEntity<List<Transakcija>> obracunZaradeTransakcije(List<ObracunTransakcijeRequest> obracunTransakcijeRequests, @RequestHeader("Authorization") String token);

    @GetMapping(value = "/sifraTransakcije/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SifraTransakcijeResponse> getById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}
