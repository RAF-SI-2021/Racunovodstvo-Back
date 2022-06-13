package raf.si.racunovodstvo.knjizenje.services.impl;

import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IBilansService {

    List<BilansResponse> findBrutoBilans(String brojKontaOd, String brojKontaDo, Date datumOd, Date datumDo);

    Map<String,List<BilansResponse>> findBilans(List<String> startsWith, List<Date> datumiOd, List<Date> datumiDo);
}
