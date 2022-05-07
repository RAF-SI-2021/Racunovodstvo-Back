package raf.si.racunovodstvo.knjizenje.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.si.racunovodstvo.knjizenje.services.SifraTransakcijeService;
import raf.si.racunovodstvo.knjizenje.services.impl.ITransakcijaService;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/transakcija")
public class TransakcijaController {

    private final ITransakcijaService transakcijaService;
    private final SifraTransakcijeService sifraTransakcijeService;

    public TransakcijaController(ITransakcijaService transakcijaService, SifraTransakcijeService sifraTransakcijeService) {
        this.transakcijaService = transakcijaService;
        this.sifraTransakcijeService = sifraTransakcijeService;
    }
}
