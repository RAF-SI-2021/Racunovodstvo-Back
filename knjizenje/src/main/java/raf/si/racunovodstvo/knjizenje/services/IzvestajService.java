package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converter.BilansSchemaConverter;
import raf.si.racunovodstvo.knjizenje.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.knjizenje.feign.UserFeignClient;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.reports.BilansTableContent;
import raf.si.racunovodstvo.knjizenje.reports.Reports;
import raf.si.racunovodstvo.knjizenje.reports.TableReport;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.responses.UserResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IBilansService;
import raf.si.racunovodstvo.knjizenje.services.impl.IIzvestajService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IzvestajService implements IIzvestajService {

    private final IBilansService bilansService;
    private final PreduzeceFeignClient preduzeceFeignClient;
    private final UserFeignClient userFeignClient;

    public IzvestajService(IBilansService bilansService,
                           BilansSchemaConverter bilansSchemaConverter,
                           PreduzeceFeignClient preduzeceFeignClient,
                           UserFeignClient userFeignClient) {
        this.bilansService = bilansService;
        this.preduzeceFeignClient = preduzeceFeignClient;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public Reports makeBrutoBilansTableReport(String token, String title, Date datumOd, Date datumDo, String brojKontaOd, String brojKontaDo) {
        List<BilansResponse> bilansResponseListMap = bilansService.findBrutoBilans(brojKontaOd, brojKontaDo, datumOd, datumDo);

        BilansTableContent bilansTableContent = new BilansTableContent(Map.of("",bilansResponseListMap), false);
        String sums = bilansTableContent.getSums();
        String name = getCurrentUsername(token);
        return new TableReport(name, title, sums, bilansTableContent.getColumns(),bilansTableContent.getRows());
    }

    @Override
    public Reports makeBilansTableReport(Long preduzeceId,
                                         String token,
                                         String title,
                                         List<Date> datumiOd,
                                         List<Date> datumiDo,
                                         List<String> brojKontaStartsWith, boolean isBilansUspeha) {

        Map<String, List<BilansResponse>> bilansResponseListMap = bilansService.findBilans(brojKontaStartsWith, datumiOd, datumiDo);
        BilansTableContent bilansTableContent = new BilansTableContent(bilansResponseListMap,isBilansUspeha);

        String preduzece = generatePreduzeceString(preduzeceId, token);
        String footer = bilansTableContent.getSums() + "\n\n\n" + preduzece;
        String name = getCurrentUsername(token);

        return new TableReport(name, title, footer, bilansTableContent.getColumns(), bilansTableContent.getRows());
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
