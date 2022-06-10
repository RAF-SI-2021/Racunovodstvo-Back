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
                                  List<String> brojKontaStartsWith);
}
