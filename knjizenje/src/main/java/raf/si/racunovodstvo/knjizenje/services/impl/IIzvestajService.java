package raf.si.racunovodstvo.knjizenje.services.impl;

import raf.si.racunovodstvo.knjizenje.reports.Reports;

import java.util.Date;
import java.util.List;

public interface IIzvestajService {

    Reports makeBrutoBilansTableReport(String token, String title, Date datumOd, Date datumDo, String brojKontaOd, String brojKontaDo);

    Reports makeBilansTableReport(Long preduzeceId,
                                  String token,
                                  String title,
                                  List<Date> datumiOd,
                                  List<Date> datumiDo,
                                  List<String> brojKontaStartsWith, boolean isBilansUspeha);

    Reports makePromenaNaKapitalTableReport(int godina1, int godina2, String opis);

    Reports makeStatickiIzvestajOTransakcijamaTableReport(long preduzeceId, String naslov, Date pocetniDatum, Date krajniDatum, String token);

    Reports makeSifraTransakcijaTableReport(String title, String sort, String token);
}
