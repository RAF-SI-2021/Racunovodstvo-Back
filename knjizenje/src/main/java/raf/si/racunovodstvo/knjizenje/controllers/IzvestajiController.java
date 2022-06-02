package raf.si.racunovodstvo.knjizenje.controllers;

import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raf.si.racunovodstvo.knjizenje.services.impl.IIzvestajService;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/izvestaji")
public class IzvestajiController {

    private final IIzvestajService izvestajService;

    public IzvestajiController(IIzvestajService izvestajService) {
        this.izvestajService = izvestajService;
    }

    @GetMapping(path = "/bruto", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getBrutoBilans(@RequestParam String title,
                                            @RequestParam String brojKontaOd,
                                            @RequestParam String brojKontaDo,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datumOd,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datumDo,
                                            @RequestHeader("Authorization") String token) throws DocumentException {
        byte[] pdf = izvestajService.makeBrutoBilansTableReport(token, title, datumOd, datumDo, brojKontaOd, brojKontaDo).getReport();
        return ResponseEntity.ok(pdf);
    }

    @GetMapping(path = "/stanje", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getBilansStanja(@RequestParam(required = false, defaultValue = "1") Long preduzece,
                                             @RequestParam String title,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiOd,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiDo,
                                             @RequestHeader("Authorization") String token) throws DocumentException {
        List<String> brojKontaStartsWith = List.of("5", "6");
        byte[] pdf =
            izvestajService.makeBilansTableReport(preduzece, token, title, datumiOd, datumiDo, brojKontaStartsWith).getReport();
        return ResponseEntity.ok(pdf);
    }

    @GetMapping(path = "/uspeh", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getBilansUspeha(@RequestParam(required = false, defaultValue = "1") Long preduzece,
                                             @RequestParam String title,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiOd,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<Date> datumiDo,
                                             @RequestHeader("Authorization") String token) throws DocumentException {
        List<String> brojKontaStartsWith = List.of("0", "1", "2", "3", "4");

        byte[] pdf =
            izvestajService.makeBilansTableReport(preduzece, token, title, datumiOd, datumiDo, brojKontaStartsWith).getReport();
        return ResponseEntity.ok(pdf);
    }
}
