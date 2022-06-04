package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.model.Koeficijent;
import raf.si.racunovodstvo.preduzece.model.Plata;
import raf.si.racunovodstvo.preduzece.model.Zaposleni;
import raf.si.racunovodstvo.preduzece.repositories.PlataRepository;
import raf.si.racunovodstvo.preduzece.requests.PlataRequest;
import raf.si.racunovodstvo.preduzece.specifications.RacunSpecification;
import raf.si.racunovodstvo.preduzece.specifications.SearchCriteria;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PlataServiceTest {

    @InjectMocks
    private PlataService plataService;
    @Mock
    private PlataRepository plataRepository;
    @Mock
    private KoeficijentService koeficijentService;
    @Mock
    private ZaposleniService zaposleniService;

    private static final Long MOCK_ID = 1L;
    private static final Date MOCK_DATE = new Date();
    private static final String MOCK_SEARCH_KEY = "MOCK_KEY";
    private static final String MOCK_SEARCH_VALUE = "MOCK_VALUE";
    private static final String MOCK_SEARCH_OPERATION = "MOCK_OPERATION";

    private Koeficijent koeficijent;

    @BeforeEach
    public void setup() {
        koeficijent = new Koeficijent();
        koeficijent.setKoeficijentPoreza(1d);
        koeficijent.setNezaposlenost1(2d);
        koeficijent.setNezaposlenost2(10d);
        koeficijent.setPenzionoOsiguranje1(5d);
        koeficijent.setPenzionoOsiguranje2(50d);
        koeficijent.setNajnizaOsnovica(1d);
        koeficijent.setNajvisaOsnovica(1d);
        koeficijent.setZdravstvenoOsiguranje1(5d);
        koeficijent.setZdravstvenoOsiguranje2(5d);
        koeficijent.setPoreskoOslobadjanje(23.4);


    }


    @Test
    void save() {
        Plata plata = new Plata();
        given(plataRepository.save(plata)).willReturn(plata);

        assertEquals(plata, plataService.save(plata));
    }

    @Test
    void testSave() {
        Zaposleni zaposleni = new Zaposleni();
        zaposleni.setZaposleniId(MOCK_ID);
        List<Plata> plataList = new ArrayList<>();

        Plata plata = new Plata();
        plata.setNetoPlata(100000.0);
        plata.setZaposleni(zaposleni);
        plata.setDatumOd(new Date());
        plata.setDatumDo(null);
        plataList.add(plata);

        PlataRequest plataRequest = new PlataRequest(1L, 500.0, new Date(), MOCK_ID);

        given(zaposleniService.findById(MOCK_ID)).willReturn(Optional.of(zaposleni));
        given(plataRepository.save(any(Plata.class))).willReturn(plata);
        given(plataRepository.findAll()).willReturn(plataList);
        given(koeficijentService.getCurrentKoeficijent()).willReturn(koeficijent);

        assertEquals(plata, plataService.save(plataRequest));
    }

    @Test
    void testSaveException(){
        Zaposleni zaposleni = new Zaposleni();

        Plata plata = new Plata();
        plata.setNetoPlata(100000.0);
        plata.setZaposleni(zaposleni);
        plata.setDatumOd(new Date());
        plata.setDatumDo(null);

        PlataRequest plataRequest = new PlataRequest(1L, 500.0, new Date(), MOCK_ID);

        given(zaposleniService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> plataService.save(plataRequest));
    }

    @Test
    void findById() {
        Plata plata = new Plata();
        given(plataRepository.findByPlataId(MOCK_ID)).willReturn(Optional.of(plata));

        assertEquals(plata, plataService.findById(MOCK_ID).get());
    }

    @Test
    void findAll() {
        List<Plata> plataList = new ArrayList<>();
        given(plataRepository.findAll()).willReturn(plataList);

        assertEquals(plataList, plataService.findAll());
    }

    @Test
    void testFindAll() {
        List<Plata> plataList = new ArrayList<>();
        Specification<Plata> specification =
                new RacunSpecification<>(new SearchCriteria(MOCK_SEARCH_KEY, MOCK_SEARCH_VALUE, MOCK_SEARCH_OPERATION));

        given(plataRepository.findAll(specification)).willReturn(plataList);

        assertEquals(plataList, plataService.findAll(specification));
    }

    @Test
    void deleteById() {
        plataService.deleteById(MOCK_ID);
        then(plataRepository).should(times(1)).deleteById(MOCK_ID);

    }

    @Test
    void findByZaposleniZaposleniId() {
        List<Plata> plataList = new ArrayList<>();
        given(plataRepository.findByZaposleniZaposleniId(MOCK_ID)).willReturn(plataList);

        assertEquals(plataList, plataService.findByZaposleniZaposleniId(MOCK_ID));
    }

    @Test
    void findPlatabyDatumAndZaposleniTest() {
        Plata plata = new Plata();
        Zaposleni zaposleni = new Zaposleni();
        given(plataRepository.findPlatabyDatumAndZaposleni(MOCK_DATE, zaposleni)).willReturn(plata);

        assertEquals(plata, plataService.findPlatabyDatumAndZaposleni(MOCK_DATE, zaposleni));
    }
}