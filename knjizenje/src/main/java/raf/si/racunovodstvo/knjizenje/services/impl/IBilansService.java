package raf.si.racunovodstvo.knjizenje.services.impl;

import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;

import java.util.Date;
import java.util.List;

public interface IBilansService {

    List<BilansResponse> findBrutoBilans(String brojKontaOd, String brojKontaDo, Date datumOd, Date datumDo);

    List<BilansResponse> findBilans(List<String> startsWith, List<Date> datumiOd, List<Date> datumiDo);
}
