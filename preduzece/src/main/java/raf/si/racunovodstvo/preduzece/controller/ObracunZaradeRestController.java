package raf.si.racunovodstvo.preduzece.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.preduzece.feign.TransakcijeFeignClient;
import raf.si.racunovodstvo.preduzece.jobs.ObracunZaradeJob;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaradeConfigRequest;
import raf.si.racunovodstvo.preduzece.responses.ObracunZaradeConfigResponse;
import raf.si.racunovodstvo.preduzece.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.preduzece.services.impl.ObracunZaposleniService;
import raf.si.racunovodstvo.preduzece.services.impl.ObracunZaradeService;
import raf.si.racunovodstvo.preduzece.validation.groups.OnCreate;

import javax.persistence.EntityNotFoundException;
import javax.print.attribute.standard.Media;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import java.time.DateTimeException;
import java.util.Date;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/obracun_zarade_config")
public class ObracunZaradeRestController {
    private final ObracunZaradeJob obracunZaradeJob;
    private final TransakcijeFeignClient transakcijeFeignClient;
    private final ObracunZaposleniService obracunZaposleniService;

    public ObracunZaradeRestController(ObracunZaradeJob obracunZaradeJob, TransakcijeFeignClient transakcijeFeignClient, ObracunZaposleniService obracunZaposleniService) {
        this.obracunZaradeJob = obracunZaradeJob;
        this.transakcijeFeignClient = transakcijeFeignClient;
        this.obracunZaposleniService = obracunZaposleniService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ObracunZaradeConfigResponse> setConfig(@Validated(OnCreate.class) @RequestBody ObracunZaradeConfigRequest obracunZaradeConfigRequest,  @RequestHeader("Authorization") String token) {

        try {

            ResponseEntity<SifraTransakcijeResponse> response =
                    transakcijeFeignClient.getById(obracunZaradeConfigRequest.getSifraTransakcijeId(), token);

            if(response.getStatusCodeValue() != 200){
                throw new EntityNotFoundException();
            }

            obracunZaradeJob.setSifraTransakcijeId(obracunZaradeConfigRequest.getSifraTransakcijeId());
            obracunZaradeJob.setDayOfMonth(obracunZaradeConfigRequest.getDayOfMonth());

            ObracunZaradeConfigResponse obracunZaradeConfigResponse = new ObracunZaradeConfigResponse();
            obracunZaradeConfigResponse.setSifraTransakcije(response.getBody());
            obracunZaradeConfigResponse.setDayOfMonth(obracunZaradeConfigRequest.getDayOfMonth());


            return ResponseEntity.ok(obracunZaradeConfigResponse);

        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ObracunZaradeConfigResponse> getConfig( @RequestHeader("Authorization") String token) {


        if(obracunZaradeJob.getSifraTransakcijeId() == 0){
            throw new RuntimeException("Nije postavljena sifra transakcije!");
        }
        ResponseEntity<SifraTransakcijeResponse> response = transakcijeFeignClient.getById(obracunZaradeJob.getSifraTransakcijeId(), token);

        if(response.getStatusCodeValue() != 200){
            throw new EntityNotFoundException();
        }

        ObracunZaradeConfigResponse obracunZaradeConfigResponse = new ObracunZaradeConfigResponse();
        obracunZaradeConfigResponse.setSifraTransakcije(response.getBody());
        obracunZaradeConfigResponse.setDayOfMonth(obracunZaradeJob.getDayOfMonth());

        return ResponseEntity.ok(obracunZaradeConfigResponse);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createObracunZarade(@NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datum, @NotNull @RequestParam Long idTransakcije) {
        return ResponseEntity.ok(obracunZaposleniService.makeObracun(datum, idTransakcije));
    }
}

