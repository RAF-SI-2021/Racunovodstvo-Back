package raf.si.racunovodstvo.knjizenje.reports;

import raf.si.racunovodstvo.knjizenje.converter.BilansSchemaConverter;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class BilansTableContent {
    public static final List<String> BILANS_COLUMNS_SINGLE_PERIOD = List.of("Konto", "Stavki", "Naziv", "Duguje", "Potrazuje", "Saldo");
    public static final List<String> BILANS_COLUMNS_MULTIPLE_PERIODS = List.of("Konto", "Stavki", "Naziv");
    private List<List<String>> rows;
    private List<String> columns;

    private String sums;

    private String generateBalansUspeha(List<BilansResponse> bilansResponseList) {
        Double rashodi = bilansResponseList
                .stream()
                .filter(bilansResponse -> bilansResponse.getBrojKonta().equals("5")).collect(Collectors.toList()).get(0).getSaldo();

        Double prihodi = bilansResponseList
                .stream()
                .filter(bilansResponse -> bilansResponse.getBrojKonta().equals("5")).collect(Collectors.toList()).get(0).getSaldo();

        return String.format("Ukupni prihodi: %d, Ukupni rashodi: %d, Balans uspeha %d", prihodi,rashodi,prihodi-rashodi);
    }
    private String generateSumsString(List<BilansResponse> bilansResponseList) {
        Long brojStavki = 0L;
        Double duguje = 0.0;
        Double potrazuje = 0.0;
        Double saldo = 0.0;
        for (BilansResponse bilansResponse : bilansResponseList) {
            if(bilansResponse.getBrojKonta().length() == 1){
                brojStavki += bilansResponse.getBrojStavki();
                duguje += bilansResponse.getDuguje();
                potrazuje += bilansResponse.getPotrazuje();
                saldo += bilansResponse.getSaldo();
            }
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


    public BilansTableContent(Map<String, List<BilansResponse>> bilansResponseListMap, boolean isBilansUspeha){

            BilansSchemaConverter bilansSchemaConverter = new BilansSchemaConverter();

            var optionalFirstBilansResponseList = bilansResponseListMap.entrySet()
                    .stream()
                    .findFirst();


            List<BilansResponse> firstBilansResponseList = new ArrayList<BilansResponse>() ;
            if(optionalFirstBilansResponseList.isPresent()){
                firstBilansResponseList.addAll(optionalFirstBilansResponseList.get().getValue());
            }

                this.columns = bilansResponseListMap.size() <= 1 ?
                    BILANS_COLUMNS_SINGLE_PERIOD:
                    BILANS_COLUMNS_MULTIPLE_PERIODS;

                this.rows = new ArrayList<>();
                firstBilansResponseList.forEach(bilansResponse -> {
                    var line = bilansSchemaConverter.convert(bilansResponse).getFields(this.columns);
                    this.rows.add(line);
                });

                if(bilansResponseListMap.size() == 1){
                    this.sums =  isBilansUspeha ?
                            generateBalansUspeha(firstBilansResponseList) :
                            generateSumsString(firstBilansResponseList);
                    return;
                }

                bilansResponseListMap.keySet().forEach(period -> {
                    this.columns.add("Satdo " + period);
                    for(int i = 0; i < firstBilansResponseList.size(); i++){
                        String periodSaldo = bilansResponseListMap.get(period).get(i).getSaldo().toString();
                        this.rows.get(i).add(periodSaldo);
                    }
                });

                List<String> sumLines = new ArrayList<>();
                bilansResponseListMap.forEach((period, list) -> {
                    String line = isBilansUspeha ? generateBalansUspeha(list):generateSumsString(list);
                    sumLines.add(String.format("%s: %s",period, line));
                });

                this.sums = String.join("\n",sumLines);

    }

    public List<String> getColumns() {
        return columns;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public String getSums() {
        return sums;
    }
}
