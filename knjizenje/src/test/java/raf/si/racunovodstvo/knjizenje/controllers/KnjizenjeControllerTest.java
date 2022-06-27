package raf.si.racunovodstvo.knjizenje.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.requests.KnjizenjeRequest;
import raf.si.racunovodstvo.knjizenje.responses.AnalitickaKarticaResponse;
import raf.si.racunovodstvo.knjizenje.services.KnjizenjeService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KnjizenjeControllerTest {

    @InjectMocks
    private KnjizenjeController knjizenjeController;
    @Mock
    private KnjizenjeService knjizenjeService;

    private static final Long MOCK_ID = 1L;
    private static final Date MOCK_DATUM_OD = new Date();
    private static final Date MOCK_DATUM_DO = new Date();
    private static final String MOCK_BROJ_KONTA = "MOCK_BROJ_KONTA";
    private static final String MOCK_SORT = "-datumKnjizenja";

    @Test
    void createDnevnikKnjizenja() {
        ResponseEntity<?> responseEntity = knjizenjeController.createDnevnikKnjizenja(new KnjizenjeRequest());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateDnevnikKnjizenja() {
        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(MOCK_ID);
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.of(knjizenje));
        ResponseEntity<?> responseEntity = knjizenjeController.updateDnevnikKnjizenja(knjizenje);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateDnevnikKnjizenjaException() {
        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(MOCK_ID);
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> knjizenjeController.updateDnevnikKnjizenja(knjizenje));
    }

    @Test
    void deleteDnevnikKnjizenja() {
        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(MOCK_ID);
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.of(knjizenje));
        ResponseEntity<?> responseEntity = knjizenjeController.deleteDnevnikKnjizenja(MOCK_ID);

        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteDnevnikKnjizenjaException() {
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> knjizenjeController.deleteDnevnikKnjizenja(MOCK_ID));
    }

    @Test
    void getDnevnikKnjizenjaId() {
        Knjizenje knjizenje = new Knjizenje();
        knjizenje.setKnjizenjeId(MOCK_ID);
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.of(knjizenje));
        ResponseEntity<?> responseEntity = knjizenjeController.getDnevnikKnjizenjaId(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getDnevnikKnjizenjaIdException() {
        given(knjizenjeService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> knjizenjeController.getDnevnikKnjizenjaId(MOCK_ID));
    }

    @Test
    void search() {
        String search = "knjizenjeId:1";
        Integer page = 1;
        Integer size = 50;
        String[] sort = new String[1];
        sort[0] = "1";

        ResponseEntity<?> responseEntity = knjizenjeController.search(search, page, size, sort);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void searchException() {
        String search = "abc";
        Integer page = 1;
        Integer size = 50;
        String[] sort = new String[1];
        sort[0] = "1";

        assertThrows(OperationNotSupportedException.class, () -> knjizenjeController.search(search, page, size, sort));
    }

    @Test
    void findAll() {
        ResponseEntity<?> responseEntity = knjizenjeController.findAll();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getKontoByKnjizenjeIdTest() {
        List<Konto> expected = new ArrayList<>();
        given(knjizenjeService.findKontoByKnjizenjeId(MOCK_ID)).willReturn(expected);
        ResponseEntity<List<Konto>> response = (ResponseEntity<List<Konto>>) knjizenjeController.getKontoByKnjizenjeId(MOCK_ID);
        assertEquals(response.getBody(), expected);
    }

    @Test
    void getAnalitickeKarticeTest() {
        List<AnalitickaKarticaResponse> expected = new ArrayList<>();
        Page<AnalitickaKarticaResponse> pages = Mockito.mock(Page.class);
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(0, 50, new String[]{MOCK_SORT});
        given(knjizenjeService.findAllAnalitickeKarticeResponse(
                pageSort,
                MOCK_BROJ_KONTA,
                MOCK_DATUM_OD,
                MOCK_DATUM_DO,
                MOCK_ID
        )).willReturn(pages);
        given(pages.getContent()).willReturn(expected);

        List<AnalitickaKarticaResponse> result =
                 knjizenjeController.getAnalitickeKartice(MOCK_BROJ_KONTA,
                       MOCK_DATUM_OD,
                       MOCK_DATUM_DO,
                       MOCK_ID,
                       0,
                       50,
                       new String[] {MOCK_SORT}).getBody().getContent();
        assertEquals(expected, result);
    }
}
