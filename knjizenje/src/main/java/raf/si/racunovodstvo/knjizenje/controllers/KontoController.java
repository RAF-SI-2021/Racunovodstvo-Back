package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.services.KontoService;
import raf.si.racunovodstvo.knjizenje.services.impl.IService;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/konto")
public class KontoController {

    private final IService<Konto, Long> kontoService;

    public KontoController(KontoService kontoService) {
        this.kontoService = kontoService;
    }
}
