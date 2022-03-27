package rs.raf.demo.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.demo.model.KontnaGrupa;
import rs.raf.demo.services.IService;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/konto")
public class KontnaGrupaRestService {

    private final IService<KontnaGrupa, Long> kontoService;

    public KontnaGrupaRestService(IService<KontnaGrupa, Long> kontoService) {
        this.kontoService = kontoService;
    }
}
