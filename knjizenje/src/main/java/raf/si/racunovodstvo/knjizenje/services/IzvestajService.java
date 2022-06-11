package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.BilansSchemaConverter;
import raf.si.racunovodstvo.knjizenje.feign.PreduzeceFeignClient;
import raf.si.racunovodstvo.knjizenje.feign.UserFeignClient;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.reports.Reports;
import raf.si.racunovodstvo.knjizenje.reports.ReportsConstants;
import raf.si.racunovodstvo.knjizenje.reports.TableReport;
import raf.si.racunovodstvo.knjizenje.reports.schema.BilansSchema;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;
import raf.si.racunovodstvo.knjizenje.responses.UserResponse;
import raf.si.racunovodstvo.knjizenje.services.helpers.SifraTransakcijaHelper;
import raf.si.racunovodstvo.knjizenje.services.helpers.StatickiIzvestajOTransakcijamaHelper;
import raf.si.racunovodstvo.knjizenje.services.impl.IBilansService;
import raf.si.racunovodstvo.knjizenje.services.impl.IIzvestajService;
import raf.si.racunovodstvo.knjizenje.services.impl.ISifraTransakcijeService;
import raf.si.racunovodstvo.knjizenje.services.impl.ITransakcijaService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

@Service
public class IzvestajService implements IIzvestajService {

    private final IBilansService bilansService;
    private final IConverter<BilansResponse, BilansSchema> bilansSchemaConverter;
    private final PreduzeceFeignClient preduzeceFeignClient;
    private final ITransakcijaService transakcijaService;
    private final ISifraTransakcijeService sifraTransakcijeService;
    private final UserFeignClient userFeignClient;
    private final SearchUtil searchUtil;

    public IzvestajService(IBilansService bilansService,
                           BilansSchemaConverter bilansSchemaConverter,
                           PreduzeceFeignClient preduzeceFeignClient,
                           ITransakcijaService transakcijaService,
                           ISifraTransakcijeService sifraTransakcijeService,
                           UserFeignClient userFeignClient) {
        this.bilansService = bilansService;
        this.bilansSchemaConverter = bilansSchemaConverter;
        this.preduzeceFeignClient = preduzeceFeignClient;
        this.transakcijaService = transakcijaService;
        this.sifraTransakcijeService = sifraTransakcijeService;
        this.userFeignClient = userFeignClient;
        this.searchUtil = new SearchUtil();
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

    @Override
    public Reports makeStatickiIzvestajOTransakcijamaTableReport(long preduzeceId, String naslov, Date pocetniDatum, Date krajniDatum, String token) {
        Preduzece preduzece = preduzeceFeignClient.getPreduzeceById(preduzeceId, token).getBody();

        if (preduzece == null) {
            throw new EntityNotFoundException();
        }

        String filter = createTransakcijeFilter(preduzeceId, pocetniDatum, krajniDatum);
        Page<TransakcijaResponse> transakcijaResponses = transakcijaService.search(searchUtil.getSpec(filter), Pageable.unpaged(), token);
        return new StatickiIzvestajOTransakcijamaHelper(naslov, preduzece, transakcijaResponses.getContent()).makeTableReport();
    }

    @Override
    public Reports makeSifraTransakcijaTableReport(String title, String[] sort, String token) {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(0, Integer.MAX_VALUE, sort);
        Page<SifraTransakcijeResponse> sifraTransakcijeResponses = sifraTransakcijeService.search(searchUtil.getSpec("sifraTransakcijeId>0"), pageSort, token);
        return new SifraTransakcijaHelper(title, sifraTransakcijeResponses.getContent()).makeReport();
    }

    private String createTransakcijeFilter(long preduzeceId, Date pocetniDatum, Date krajniDatum) {
        String filter = "preduzeceId:" + preduzeceId;
        if (pocetniDatum == null || krajniDatum == null) {
            return filter;
        }
        return filter + ",datumTransakcije>" + pocetniDatum + ",datumTransakcije<" + krajniDatum;
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
