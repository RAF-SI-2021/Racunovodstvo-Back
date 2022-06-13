package raf.si.racunovodstvo.knjizenje.services;

import raf.si.racunovodstvo.knjizenje.exceptions.ReportNotReadyException;
import raf.si.racunovodstvo.knjizenje.reports.Reports;
import raf.si.racunovodstvo.knjizenje.reports.TableReport;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.IBilansService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

// http://www.cekos.rs/sites/default/files/media/obrasci/15085201-05.pdf
public class PromenaNaKapitalHelper {

    private int godina1;

    private int godina2;

    private String opis;

    private IBilansService bilansService;

    private LinkedList<String> headers = new LinkedList(List.of("Osnovni kapital (grupa 30 bez 306 i 309)",
                                                                "Ostali osnovni kapital (309)",
                                                                "Upisani a neplaceni kapital (grupa 31)",
                                                                "Emisiona premija i rezerve (306 i grupa 32)",
                                                                "Rev. rez. i ner. dob. i gub. (grupa 33)",
                                                                "Nerasporedjeni dobitak (grupa 34)",
                                                                "Gubitak (grupa 35)",
                                                                "Ukupno (2+3+4+5+6+7-8"));

    private List<String> firstColumnPattern = List.of("Stanje na dan 01.01.%d.",
                                                      "Stanje na dan 31.12.%d.",
                                                      "Promene u %d. godini");

    public PromenaNaKapitalHelper(int godina1, int godina2, String opis, IBilansService bilansService) {
        int godina = Calendar.getInstance().get(Calendar.YEAR);
        if (godina <= godina1 || godina <= godina2) {
            throw new ReportNotReadyException();
        }
        this.godina1 = godina1;
        this.godina2 = godina2;
        this.opis = opis;
        this.bilansService = bilansService;
    }

    public Reports makePromenaNaKapitalTableReport() {
        List<List<Double>> rows = calculateForGodina(godina1);
        rows.addAll(calculateForGodina(godina2));
        List<List<String>> listOfStringRows = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            List<String> stringRow = rows.get(i).stream().map(String::valueOf).collect(Collectors.toList());
            List<String> newRow = addFirstColumn(i, stringRow, godina1, godina2);
            listOfStringRows.add(newRow);
        }
        headers.addFirst(opis);
        return new TableReport("author", "title", "footer", headers, listOfStringRows);
    }

    private List<String> addFirstColumn(int i, List<String> stringRow, int godina1, int godina2) {
        int godina = i / 3 == 0 ? godina1 : godina2;
        LinkedList<String> linkedList = new LinkedList<>(stringRow);
        linkedList.addFirst(String.format(firstColumnPattern.get(i % 3), godina));
        return linkedList;
    }

    private List<List<Double>> calculateForGodina(int godina) {
        Date startOfGodina = getDateForDayMonthYear(1, 1, godina);
        Date endOfGodina = getDateForDayMonthYear(31, 12, godina);
        List<List<Double>> rows = new ArrayList<>();
        rows.add(calculateRow(startOfGodina));
        rows.add(calculateRow(endOfGodina));
        rows.add(calculateRowDiff(rows.get(0), rows.get(1)));
        return rows;
    }

    private Date getDateForDayMonthYear(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    private List<Double> calculateRow(Date dateLimit) {
        List<Double> row = new ArrayList<>();
        row.add(calculate2ndColumnHelper(dateLimit));
        row.add(calculateForBrojKonta(dateLimit, "309"));
        row.add(calculateForBrojKonta(dateLimit, "31"));
        row.add(calculate5thColumnHelper(dateLimit));
        row.add(calculateForBrojKonta(dateLimit, "33"));
        row.add(calculateForBrojKonta(dateLimit, "34"));
        row.add(calculateForBrojKonta(dateLimit, "35"));
        double sum = row.stream().mapToDouble(value -> value).sum() - 2 * row.get(6);
        row.add(sum);
        return row;
    }

    private List<Double> calculateRowDiff(List<Double> pocetakGodine, List<Double> krajGodine) {
        List<Double> diff = new ArrayList<>();
        Iterator<Double> i1 = pocetakGodine.iterator();
        Iterator<Double> i2= krajGodine.iterator();
        while(i1.hasNext() && i2.hasNext())
        {
            diff.add(i1.next() - i2.next());
        }
        return diff;
    }

    private double calculate2ndColumnHelper(Date dateLimit) {
        double konto30Saldo = calculateForBrojKonta(dateLimit, "30");
        double konto306Saldo = calculateForBrojKonta(dateLimit, "306");
        double konto309Saldo = calculateForBrojKonta(dateLimit, "309");

        return konto30Saldo - konto306Saldo - konto309Saldo;
    }

    private double calculate5thColumnHelper(Date dateLimit) {
        double konto306Saldo = calculateForBrojKonta(dateLimit, "306");
        double konto32Saldo = calculateForBrojKonta(dateLimit, "32");

        return konto32Saldo + konto306Saldo;
    }

    private double calculateForBrojKonta(Date dateLimit, String brojKonta) {
        List<BilansResponse> bilansResponse = bilansService.findBrutoBilans(brojKonta, brojKonta, new Date(0), dateLimit);
        double saldoZaKonto = bilansResponse.stream()
                                            .filter(br -> br.getBrojKonta().equals(brojKonta))
                                            .mapToDouble(br -> br.getSaldo()).sum();
        return saldoZaKonto;
    }
}
