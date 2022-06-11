package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.repositories.KontnaGrupaRepository;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IBilansService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static raf.si.racunovodstvo.knjizenje.utils.Utils.periodToString;

@Service
public class BilansService implements IBilansService {

    private final KontnaGrupaRepository kontnaGrupaRepository;


    @Autowired
    public BilansService(KontnaGrupaRepository kontnaGrupaRepository) {
        this.kontnaGrupaRepository = kontnaGrupaRepository;
    }

    @Override
    public Map<String,List<BilansResponse>> findBilans(List<String> startsWith, List<Date> datumiOd, List<Date> datumiDo) {
        Map<String,List<BilansResponse>> bilansLists = new HashMap<>();



        for (int i = 0; i < datumiDo.size() && i < datumiOd.size(); i++) {
            Set<BilansResponse> bilansSet = new HashSet<>();

            String period = periodToString(datumiOd.get(i),datumiDo.get(i));

            bilansSet.addAll(kontnaGrupaRepository.findAllStartingWith(startsWith, datumiOd.get(i), datumiDo.get(i)));
            bilansLists.put(period,new ArrayList<>(bilansSet));
        }
        bilansLists.forEach((key,list) -> {
            list.sort(Comparator.comparing(BilansResponse::getBrojKonta).reversed());
            sumBilans(list);
            sortBilans(list);
        });

        return bilansLists;
    }

    @Override
    public List<BilansResponse> findBrutoBilans(String brojKontaOd, String brojKontaDo, Date datumOd, Date datumDo) {
        List<BilansResponse> bilansList = kontnaGrupaRepository.findAllForBilans(brojKontaOd, brojKontaDo, datumOd, datumDo);
        bilansList.sort(Comparator.comparing(BilansResponse::getBrojKonta).reversed());

        sumBilans(bilansList);
        sortBrutoBilans(bilansList);
        return bilansList;
    }

    private void sumBilans(List<BilansResponse> bilansList) {
        Map<String, Double> dugujeMap = new HashMap<>();
        Map<String, Double> potrazujeMap = new HashMap<>();
        Map<String, Long> brojStavkiMap = new HashMap<>();

        bilansList.forEach(bilansResponse -> {
            String brojKonta = bilansResponse.getBrojKonta();
            int length = brojKonta.length();
            if (length <= 3) {
                bilansResponse.setDuguje(bilansResponse.getDuguje() + dugujeMap.getOrDefault(brojKonta, 0.0));
                bilansResponse.setPotrazuje(bilansResponse.getPotrazuje() + potrazujeMap.getOrDefault(brojKonta, 0.0));
                bilansResponse.setBrojStavki(brojStavkiMap.getOrDefault(brojKonta, 0L));
                bilansResponse.setSaldo(bilansResponse.getDuguje() - bilansResponse.getPotrazuje());
            }

            String key = length > 3 ? brojKonta.substring(0, 3) : brojKonta.substring(0, brojKonta.length() - 1);
            dugujeMap.put(key, dugujeMap.getOrDefault(key, 0.0) + bilansResponse.getDuguje());
            potrazujeMap.put(key, potrazujeMap.getOrDefault(key, 0.0) + bilansResponse.getPotrazuje());
            brojStavkiMap.put(key, brojStavkiMap.getOrDefault(key, 0L) + bilansResponse.getBrojStavki());
        });
    }

    private void sortBrutoBilans(List<BilansResponse> bilansList) {
        bilansList.sort((o1, o2) -> {
            String bk1 = o1.getBrojKonta();
            String bk2 = o2.getBrojKonta();
            int len1 = bk1.length();
            int len2 = bk2.length();
            if (len1 == len2) {
                return bk1.compareTo(bk2);
            }
            if (bk1.startsWith(bk2.substring(0, 1))) {
                if (len1 > 3 && len2 > 3) {
                    return bk1.compareTo(bk2);
                }
                if (len1 > len2) {
                    return bk1.startsWith(bk2) ? -1 : bk1.compareTo(bk2);
                }
                return bk2.startsWith(bk1) ? 1 : bk1.compareTo(bk2);
            }
            return bk1.substring(0, 1).compareTo(bk2.substring(0, 1));
        });
    }

    private void sortBilans(List<BilansResponse> bilansList) {
        bilansList.sort((o1, o2) -> {
            String bk1 = o1.getBrojKonta();
            String bk2 = o2.getBrojKonta();
            int len1 = bk1.length();
            int len2 = bk2.length();
            if (len1 == len2) {
                return bk1.compareTo(bk2);
            }
            if (len1 > len2) {
                return bk1.startsWith(bk2) ? -1 : bk1.compareTo(bk2);
            } else {
                return bk2.startsWith(bk1) ? -1 : bk2.compareTo(bk1);
            }
        });
    }
}

