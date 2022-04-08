package rs.raf.demo.services.impl;


import org.springframework.stereotype.Service;
import rs.raf.demo.repositories.KontoRepository;
import rs.raf.demo.requests.BilansDateRequest;
import rs.raf.demo.requests.BilansRequest;
import rs.raf.demo.responses.BilansResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BilansService {

    private final KontoRepository kontoRepository;

    public BilansService(KontoRepository kontoRepository) {
        this.kontoRepository = kontoRepository;
    }

    public List<BilansResponse> findBilansStanja(List<BilansDateRequest> datumi){
        List<BilansResponse> threeChars = kontoRepository.findBilansStanjaThirdGroup();
        List<BilansResponse> twoChars = kontoRepository.findBilansStanjaSecondGroup();
        List<BilansResponse> oneChar = kontoRepository.findBilansStanjaFirstGroup();
        List<BilansResponse> bilansStanja = new ArrayList<>();
        for(BilansDateRequest bd : datumi){
            bilansStanja.addAll(kontoRepository.findAllBilansStanja(bd.getDatumOd(),bd.getDatumDo()));
        }
        bilansStanja = sumValues(bilansStanja);
        return sortBilans(sumGroupedBilans(bilansStanja,threeChars,twoChars,oneChar));
    }

    public List<BilansResponse> findBilansUspeha(List<BilansDateRequest> datumi){
        List<BilansResponse> threeChars = kontoRepository.findBilansUspehaThirdGroup();
        List<BilansResponse> twoChars = kontoRepository.findBilansUspehaSecondGroup();
        List<BilansResponse> oneChar = kontoRepository.findBilansUspehaFirstGroup();
        List<BilansResponse> bilansUspeha = new ArrayList<>();
        for(BilansDateRequest bd : datumi){
            bilansUspeha.addAll(kontoRepository.findAllBilansUspeha(bd.getDatumOd(),bd.getDatumDo()));
        }
        bilansUspeha = sumValues(bilansUspeha);
        return sortBilans(sumGroupedBilans(bilansUspeha,threeChars,twoChars,oneChar));
    }

    public List<BilansResponse> findBrutoBilans(BilansRequest bilansRequest){
        List<BilansResponse> bilansList = getBilans(bilansRequest.getDatumOd(),bilansRequest.getDatumDo());
        List<BilansResponse> brutoBilans = new ArrayList<>();
        int i=-1,j=-1;
        for(BilansResponse b : bilansList){
            if(b.getBrojKonta().equals(bilansRequest.getKontoOd().getBrojKonta())){
                i = bilansList.indexOf(b);
            }
            if(b.getBrojKonta().equals(bilansRequest.getKontoDo().getBrojKonta()))
                j = bilansList.indexOf(b);
        }
        if(i != -1 && j!=-1)
            for(int k = i;k<=j;k++){
                brutoBilans.add(bilansList.get(k));
            }
        return sortBilans(brutoBilans);
    }

    private List<BilansResponse> getBilans(Date datumOd, Date datumDo){

        List<BilansResponse> threeChars = kontoRepository.findThirdGroup();
        List<BilansResponse> twoChars = kontoRepository.findSecondGroup();
        List<BilansResponse> oneChar = kontoRepository.findFirstGroup();
        List<BilansResponse> mandatory = kontoRepository.findOtherGroups(datumOd,datumDo);

        return sortBilans(sumGroupedBilans(mandatory,threeChars,twoChars,oneChar));
    }

    private List<BilansResponse> sortBilans(List<BilansResponse> bilansResponses){
        Collections.sort(bilansResponses, new Comparator<BilansResponse>() {
            @Override
            public int compare(BilansResponse o1, BilansResponse o2) {
                return o1.getBrojKonta().compareTo(o2.getBrojKonta());
            }
        });
        return bilansResponses;
    }

    private List<BilansResponse> sumValues(List<BilansResponse> bilansResponses){

        Map<String,BilansResponse> sum = new HashMap<>();

        for(BilansResponse b : bilansResponses){
            if(!sum.containsKey(b.getBrojKonta())){
                sum.put(b.getBrojKonta(),b);
            }else{
                Double duguje = sum.get(b.getBrojKonta()).getDuguje()+b.getDuguje();
                Double potrazuje = sum.get(b.getBrojKonta()).getPotrazuje()+b.getPotrazuje();
                Long brojStavki = sum.get(b.getBrojKonta()).getBrojStavki()+b.getBrojStavki();
                sum.put(b.getBrojKonta(),b);
            }
        }
        List<BilansResponse> summedValues = sum.values().stream().collect(Collectors.toList());

        return summedValues;
    }

    private List<BilansResponse> sumGroupedBilans(List<BilansResponse> bilansResponses, List<BilansResponse> threeChars, List<BilansResponse> twoChars,List<BilansResponse> oneChar){

        List<BilansResponse> mandatory = bilansResponses;

        for(BilansResponse b: bilansResponses){
            for(BilansResponse br : threeChars){
                if(b.getBrojKonta().startsWith(br.getBrojKonta())){
                    br.setPotrazuje(br.getPotrazuje()+b.getPotrazuje());
                    br.setBrojStavki(br.getBrojStavki()+b.getBrojStavki());
                    br.setDuguje(br.getDuguje()+b.getDuguje());
                }
            }
        }
        for(BilansResponse b: threeChars){
            for(BilansResponse br: twoChars){
                if(b.getBrojKonta().startsWith(br.getBrojKonta())){
                    br.setPotrazuje(br.getPotrazuje()+b.getPotrazuje());
                    br.setDuguje(br.getDuguje()+b.getDuguje());
                    br.setBrojStavki(br.getBrojStavki()+b.getBrojStavki());
                    b.setSaldo(b.getDuguje()-b.getPotrazuje());
                }
            }
        }
        for(BilansResponse b: twoChars){
            for(BilansResponse br: oneChar){
                if(b.getBrojKonta().startsWith(br.getBrojKonta())){
                    br.setPotrazuje(br.getPotrazuje()+b.getPotrazuje());
                    br.setDuguje(br.getDuguje()+b.getDuguje());
                    br.setBrojStavki(br.getBrojStavki()+b.getBrojStavki());
                    b.setSaldo(b.getDuguje()-b.getPotrazuje());
                }
            }
        }
        for(BilansResponse b: oneChar){
            b.setSaldo(b.getDuguje()-b.getPotrazuje());
        }

        mandatory.addAll(threeChars);
        mandatory.addAll(twoChars);
        mandatory.addAll(oneChar);
        return mandatory;
    }
}

