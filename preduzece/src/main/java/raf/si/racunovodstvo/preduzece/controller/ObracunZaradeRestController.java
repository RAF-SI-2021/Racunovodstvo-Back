package raf.si.racunovodstvo.preduzece.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.racunovodstvo.preduzece.jobs.ObracunZaradeJob;

import java.time.DateTimeException;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/obracun_zarade")
public class ObracunZaradeRestController {
    private final ObracunZaradeJob obracunZaradeJob;

    public ObracunZaradeRestController(ObracunZaradeJob obracunZaradeJob) {
        this.obracunZaradeJob = obracunZaradeJob;
    }

    @PostMapping(value = "/{dan}")
    public ResponseEntity<?> changeJobDayOfMonth(@PathVariable Integer dan) {
        try {
            obracunZaradeJob.setDayOfMonth(dan);
            return ResponseEntity.ok().build();
        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

