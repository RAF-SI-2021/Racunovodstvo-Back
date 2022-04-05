package rs.raf.demo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.raf.demo.model.Knjizenje;
import rs.raf.demo.model.Konto;
import rs.raf.demo.repositories.KnjizenjeRepository;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KnjizenjeServiceTest {

    @InjectMocks
    private KnjizenjeService knjizenjeService;

    @Mock
    private KnjizenjeRepository knjizenjeRepository;

    private Konto konto1;

    private Konto konto2;

    private Konto konto3;

    private Knjizenje knjizenje;

    @BeforeEach
    void setUp() {
        konto1 = new Konto();
        konto1.setDuguje(1000.0);
        konto1.setPotrazuje(500.0);

        konto2 = new Konto();
        konto2.setDuguje(2000.0);
        konto2.setPotrazuje(1000.0);

        konto3 = new Konto();
        konto3.setDuguje(0.0);
        konto3.setPotrazuje(1000.0);

        knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(1L);
        knjizenje.setKonto(List.of(konto1, konto2, konto3));
    }

    @Test
    public void testHappyPath() {

        when(knjizenjeRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(knjizenje));

        double sumaDuguje = knjizenjeService.getSumaDugujeZaKnjizenje(knjizenje.getKnjizenjeId());
        double sumaPotrazuje = knjizenjeService.getSumaPotrazujeZaKnjizenje(knjizenje.getKnjizenjeId());
        double saldo = knjizenjeService.getSaldoZaKnjizenje(knjizenje.getKnjizenjeId());

        assertEquals(3000, sumaDuguje);
        assertEquals(2500, sumaPotrazuje);
        assertEquals(-500, saldo);
    }

    @Test()
    public void testKnjizenjeNotFound() {
        when(knjizenjeRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(null));
        when(knjizenjeRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(null));

        assertThrows(EntityNotFoundException.class, () -> knjizenjeService.getSumaDugujeZaKnjizenje(2L));
        assertThrows(EntityNotFoundException.class, () -> knjizenjeService.getSumaPotrazujeZaKnjizenje(2L));
    }

    @Test()
    public void testDugujePotrazujeNotSet() {
        konto1 = new Konto();
        knjizenje.setKonto(List.of(konto1, konto2, konto3));
        when(knjizenjeRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(knjizenje));

        double sumaDuguje = knjizenjeService.getSumaDugujeZaKnjizenje(knjizenje.getKnjizenjeId());
        double sumaPotrazuje = knjizenjeService.getSumaPotrazujeZaKnjizenje(knjizenje.getKnjizenjeId());
        double saldo = knjizenjeService.getSaldoZaKnjizenje(knjizenje.getKnjizenjeId());

        assertEquals(2000, sumaDuguje);
        assertEquals(2000, sumaPotrazuje);
        assertEquals(0, saldo);
    }
}