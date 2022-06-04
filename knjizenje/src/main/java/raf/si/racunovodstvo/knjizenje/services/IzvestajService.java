package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converter.BilansSchemaConverter;
import raf.si.racunovodstvo.knjizenje.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.knjizenje.feign.UserFeignClient;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.reports.Reports;
import raf.si.racunovodstvo.knjizenje.reports.ReportsConstants;
import raf.si.racunovodstvo.knjizenje.reports.TableReport;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.responses.UserResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IBilansService;
import raf.si.racunovodstvo.knjizenje.services.impl.IIzvestajService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IzvestajService implements IIzvestajService {

    private final IBilansService bilansService;
    private final BilansSchemaConverter bilansSchemaConverter;
    private final PreduzeceFeignClient preduzeceFeignClient;
    private final UserFeignClient userFeignClient;

    public IzvestajService(IBilansService bilansService,
                           BilansSchemaConverter bilansSchemaConverter,
                           PreduzeceFeignClient preduzeceFeignClient,
                           UserFeignClient userFeignClient) {
        this.bilansService = bilansService;
        this.bilansSchemaConverter = bilansSchemaConverter;
        this.preduzeceFeignClient = preduzeceFeignClient;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public Reports makeBrutoBilansTableReport(String token, String title, Date datumOd, Date datumDo, String brojKontaOd, String brojKontaDo) {
        List<BilansResponse> bilansResponseList = bilansService.findBrutoBilans(brojKontaOd, brojKontaDo, datumOd, datumDo);
        List<List<String>> rows = bilansResponseList.stream()
                                                    .map(bilansResponse -> bilansSchemaConverter.convert(bilansResponse).getList())
                                                    .collect(Collectors.toList());
        String sums = generateSumsString(bilansResponseList);
        String name = getCurrentUsername(token);
        return new TableReport(name, title, sums, ReportsConstants.BILANS_COLUMNS, rows);
    }

    @Override
    public Reports makeBilansTableReport(Long preduzeceId,
                                         String token,
                                         String title,
                                         List<Date> datumiOd,
                                         List<Date> datumiDo,
                                         List<String> brojKontaStartsWith) {
        List<BilansResponse> bilansResponseList = bilansService.findBilans(brojKontaStartsWith, datumiOd, datumiDo);
        List<List<String>> rows = bilansResponseList.stream()
                                                    .map(bilansResponse -> bilansSchemaConverter.convert(bilansResponse).getList())
                                                    .collect(Collectors.toList());
        String sums = generateSumsString(bilansResponseList);
        String preduzece = generatePreduzeceString(preduzeceId, token);
        String footer = sums + "\n\n\n" + preduzece;
        String name = getCurrentUsername(token);
        return new TableReport(name, title, footer, ReportsConstants.BILANS_COLUMNS, rows);
    }

    private String generateSumsString(List<BilansResponse> bilansResponseList) {
        Long brojStavki = 0L;
        Double duguje = 0.0;
        Double potrazuje = 0.0;
        Double saldo = 0.0;
        for (BilansResponse bilansResponse : bilansResponseList) {
            brojStavki += bilansResponse.getBrojStavki();
            duguje += bilansResponse.getDuguje();
            potrazuje += bilansResponse.getPotrazuje();
            saldo += bilansResponse.getSaldo();
        }
        return "Ukupno stavki: "
            + brojStavki
            + " | Duguje ukupno: "
            + duguje
            + " | Potrazuje ukupno: "
            + potrazuje
            + " | Saldo ukupno: "
            + saldo;
    }

    private String generatePreduzeceString(Long preduzeceId, String token) {
        Preduzece preduzece = preduzeceFeignClient.getPreduzeceById(preduzeceId, token).getBody();
        if (preduzece != null) {
            return preduzece.getNaziv() + "\n" + preduzece.getAdresa() + ", " + preduzece.getGrad() + "\n";
        }
        return "";
    }


    private String getCurrentUsername(String token) {
        UserResponse user = userFeignClient.getCurrentUser(token).getBody();
        return user == null ? "" : user.getUsername();
    }
}
