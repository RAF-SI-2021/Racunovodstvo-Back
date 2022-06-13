package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.model.enums.TipTransakcije;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;

@Component
public class SifraTransakcijeReverseConverter implements IConverter<SifraTransakcije, SifraTransakcijeResponse> {

    private final ModelMapper modelMapper;

    public SifraTransakcijeReverseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SifraTransakcijeResponse convert(SifraTransakcije source) {
        SifraTransakcijeResponse transakcijaResponse = modelMapper.map(source, SifraTransakcijeResponse.class);
        calculateFields(transakcijaResponse, source);
        return transakcijaResponse;
    }

    private void calculateFields(SifraTransakcijeResponse transakcijaResponse, SifraTransakcije source) {
        transakcijaResponse.setSifraTransakcijeId(source.getSifraTransakcijeId());
        double uplata = source.getTransakcija()
                              .stream()
                              .filter(transakcija -> transakcija.getTipTransakcije().equals(TipTransakcije.UPLATA))
                              .mapToDouble(Transakcija::getIznos)
                              .sum();
        transakcijaResponse.setUplata(uplata);
        double isplata = source.getTransakcija()
                              .stream()
                              .filter(transakcija -> transakcija.getTipTransakcije().equals(TipTransakcije.ISPLATA))
                              .mapToDouble(Transakcija::getIznos)
                              .sum();
        transakcijaResponse.setIsplata(isplata);
        transakcijaResponse.setSaldo(uplata - isplata);
    }
}
