package raf.si.racunovodstvo.knjizenje.services.helpers;

import raf.si.racunovodstvo.knjizenje.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.knjizenje.reports.Reports;
import raf.si.racunovodstvo.knjizenje.reports.TableReport;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SifraTransakcijaHelper {

    private static final List<String> HEADER = List.of("Sifra transakcije", "Uplata", "Isplata", "Saldo");

    private static final Map<String, Comparator<SifraTransakcijeResponse>> sortMap =
        Map.of("uplata", Comparator.comparing(SifraTransakcijeResponse::getUplata),
               "-uplata", Comparator.comparing(SifraTransakcijeResponse::getUplata).reversed(),
               "saldo", Comparator.comparing(SifraTransakcijeResponse::getSaldo),
               "-saldo", Comparator.comparing(SifraTransakcijeResponse::getSaldo).reversed());

    private String title;
    private List<SifraTransakcijeResponse> sifraTransakcijeResponses;
    private String sort;

    public SifraTransakcijaHelper(String title, List<SifraTransakcijeResponse> content, String sort) {
        this.title = title;
        this.sifraTransakcijeResponses = content;
        this.sort = sort;
    }

    public Reports makeReport() {
        sort();
        List<List<String>> table = new ArrayList<>();
        for (SifraTransakcijeResponse sifraTransakcije : sifraTransakcijeResponses) {
            List<String> row = new ArrayList<>();
            row.add(sifraTransakcije.getSifra().toString());
            row.add(sifraTransakcije.getUplata().toString());
            row.add(sifraTransakcije.getIsplata().toString());
            row.add(sifraTransakcije.getSaldo().toString());
            table.add(row);
        }
        return new TableReport("author", String.format("Naslov: %s", title), "footer", HEADER, table);
    }

    private void sort() {
        if (sort != null && !sortMap.containsKey(sort)) {
            throw new OperationNotSupportedException("Nije moguce sortirati po " + sort);
        }
        Comparator<SifraTransakcijeResponse> comparator = sortMap.get(sort);
        List<SifraTransakcijeResponse> modifiableList = new ArrayList<>(sifraTransakcijeResponses);
        modifiableList.sort(comparator);
        this.sifraTransakcijeResponses = modifiableList;
    }
}
