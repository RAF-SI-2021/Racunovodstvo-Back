package raf.si.racunovodstvo.preduzece.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.preduzece.feign.TransakcijeFeignClient;
import raf.si.racunovodstvo.preduzece.jobs.ObracunZaradeJob;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaradeConfigRequest;
import raf.si.racunovodstvo.preduzece.responses.ObracunZaradeConfigResponse;
import raf.si.racunovodstvo.preduzece.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.preduzece.validation.groups.OnCreate;

import javax.persistence.EntityNotFoundException;
import java.time.DateTimeException;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/obracun_zarade_config")
public class ObracunZaradeRestController {
    private final ObracunZaradeJob obracunZaradeJob;
    private final  TransakcijeFeignClient transakcijeFeignClient;

    public ObracunZaradeRestController(ObracunZaradeJob obracunZaradeJob, TransakcijeFeignClient transakcijeFeignClient) {
        this.obracunZaradeJob = obracunZaradeJob;
        this.transakcijeFeignClient = transakcijeFeignClient;
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

        ResponseEntity<SifraTransakcijeResponse> response = transakcijeFeignClient.getById(obracunZaradeJob.getSifraTransakcijeId(), token);

        if(response.getStatusCodeValue() != 200){
            throw new EntityNotFoundException();
        }

        ObracunZaradeConfigResponse obracunZaradeConfigResponse = new ObracunZaradeConfigResponse();
        obracunZaradeConfigResponse.setSifraTransakcije(response.getBody());
        obracunZaradeConfigResponse.setDayOfMonth(obracunZaradeJob.getDayOfMonth());

        return ResponseEntity.ok(obracunZaradeConfigResponse);
    }
}

