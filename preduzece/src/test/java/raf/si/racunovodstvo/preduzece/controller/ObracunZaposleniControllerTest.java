package raf.si.racunovodstvo.preduzece.controller;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;
import raf.si.racunovodstvo.preduzece.model.ObracunZaposleni;
import raf.si.racunovodstvo.preduzece.requests.ObracunZaposleniRequest;
import raf.si.racunovodstvo.preduzece.services.impl.ObracunZaposleniService;
import raf.si.racunovodstvo.preduzece.utils.SearchUtil;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObracunZaposleniControllerTest {

    @InjectMocks
    private ObracunZaposleniController obracunZaposleniController;
    @Mock
    private ObracunZaposleniService obracunZaposleniService;

    @Mock
    private Specification<ObracunZaposleni> specificationMock;
    private static final String MOCK_SEARCH = "MOCK_SEARCH";
    private static final Integer MOCK_PAGE = 1;
    private static final Integer MOCK_SIZE = 1;
    private static final String[] MOCK_SORT = {"MOCK_SORT"};
    @Mock
    private SearchUtil<ObracunZaposleni> searchUtil;

    @BeforeEach
    void setUp(){
        ReflectionTestUtils.setField(obracunZaposleniController, "searchUtil", searchUtil);
    }

    @Test
    void createObracunZaposleniTest(){
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        ObracunZaposleniRequest obracunZaposleniRequest = new ObracunZaposleniRequest();
        given(obracunZaposleniService.save(obracunZaposleniRequest)).willReturn(obracunZaposleni);
        assertEquals(obracunZaposleni, obracunZaposleniController.createObracunZaposleni(obracunZaposleniRequest).getBody());
    }

    @Test
    void deleteObracunZaposleniTest(){
        obracunZaposleniController.deleteObracunZaposleni(1L);
        then(obracunZaposleniService).should(times(1)).deleteById(1L);
    }

    @Test
    void getAllObracunZaposleniTest(){
        List<ObracunZaposleni> obracunZaposlenis = new ArrayList<>();
        given(obracunZaposleniService.findAll()).willReturn(obracunZaposlenis);
        assertEquals(obracunZaposlenis, obracunZaposleniController.getAllObracunZaposleni().getBody());
    }

    @Test
    void searchTest(){
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        Page<ObracunZaposleni> obracunZaposleniPage = new PageImpl<>(List.of(obracunZaposleni));

        given(obracunZaposleniService.findAll(any(), any())).willReturn(obracunZaposleniPage);
        given(searchUtil.getSpec(MOCK_SEARCH)).willReturn(specificationMock);
        assertEquals(obracunZaposleniPage, obracunZaposleniController.search(MOCK_SEARCH, MOCK_PAGE, MOCK_SIZE, MOCK_SORT).getBody());
        then(obracunZaposleniService).should(never()).findAll(any());

    }

    @Test
    void searchEmptyTest(){
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        Page<ObracunZaposleni> obracunZaposleniPage = new PageImpl<>(List.of(obracunZaposleni));

        given(obracunZaposleniService.findAll(any())).willReturn(obracunZaposleniPage);
        assertEquals(obracunZaposleniPage, obracunZaposleniController.search(Strings.EMPTY, MOCK_PAGE, MOCK_SIZE, MOCK_SORT).getBody());
        then(obracunZaposleniService).should(never()).findAll(any(), any());

    }

    @Test
    void update(){
        ObracunZaposleni obracunZaposleni = new ObracunZaposleni();
        given(obracunZaposleniService.update(0.5, null, 1L)).willReturn(obracunZaposleni);
        assertEquals(obracunZaposleni, obracunZaposleniController.update(0.5, null, 1L).getBody());
    }
}
