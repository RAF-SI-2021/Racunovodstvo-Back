package rs.raf.demo.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.demo.services.impl.DnevnikKnjizenjaService;

@CrossOrigin
@RestController
@RequestMapping("/api/knjizenje")
public class KnjizenjeController {

    private final DnevnikKnjizenjaService dnevnikKnjizenjaService;

    public KnjizenjeController(DnevnikKnjizenjaService dnevnikKnjizenjaService) {
        this.dnevnikKnjizenjaService = dnevnikKnjizenjaService;
    }
}
