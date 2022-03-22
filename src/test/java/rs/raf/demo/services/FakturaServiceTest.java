package rs.raf.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.raf.demo.model.Faktura;
import rs.raf.demo.model.TipFakture;
import rs.raf.demo.repositories.FakturaRepository;
import rs.raf.demo.services.impl.FakturaService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FakturaServiceTest {

    @InjectMocks
    private FakturaService fakturaService;

    @Mock
    private FakturaRepository fakturaRepository;

    private List<Faktura> fakture;

    @BeforeEach
    public void setup () {
        fakture = new ArrayList<>();
    }


    @Test
    void testUlazneFakture(){
        Faktura f1 = new Faktura();
        f1.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        Faktura f2 = new Faktura();
        f2.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        Faktura f3 = new Faktura();
        f3.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        Faktura f4 = new Faktura();
        f4.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fakture.add(f1);
        fakture.add(f2);
        fakture.add(f3);
        fakture.add(f4);
        when(fakturaRepository.findAll()).thenReturn(fakture);
        List<Faktura> ulazneFakture = fakturaService.findUlazneFakture();
        assertEquals(1,ulazneFakture.size());
        for(Faktura f : ulazneFakture){
            assertEquals(TipFakture.ULAZNA_FAKTURA, f.getTipFakture());
        }
    }

    @Test
    void testIzlazneFakture(){
        Faktura f1 = new Faktura();
        f1.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        Faktura f2 = new Faktura();
        f2.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        Faktura f3 = new Faktura();
        f3.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        Faktura f4 = new Faktura();
        f4.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fakture.add(f1);
        fakture.add(f2);
        fakture.add(f3);
        fakture.add(f4);
        when(fakturaRepository.findAll()).thenReturn(fakture);
        List<Faktura> izlazneFakture = fakturaService.findIzlazneFakture();
        assertEquals(2,izlazneFakture.size());
        for(Faktura f : izlazneFakture){
            assertEquals(TipFakture.IZLAZNA_FAKTURA, f.getTipFakture());
        }

    }

    @Test
    void testAllFakture(){
        List<Faktura> fakture = new ArrayList<>();
        Faktura f1 = new Faktura();
        f1.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        Faktura f2 = new Faktura();
        f2.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        Faktura f3 = new Faktura();
        f3.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        Faktura f4 = new Faktura();
        f4.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        fakture.add(f1);
        fakture.add(f2);
        fakture.add(f3);
        fakture.add(f4);
        when(fakturaRepository.findAll()).thenReturn(fakture);
        List<Faktura> rezultat = fakturaService.findAll();
        assertEquals(4,rezultat.size());
    }

    @Test
    void testEmtyFakture(){
        when(fakturaRepository.findAll()).thenReturn(fakture);
        List<Faktura> rezultat = fakturaService.findIzlazneFakture();

        assertEquals(0, rezultat.size());
    }



}