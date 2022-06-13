package raf.si.racunovodstvo.knjizenje.controllers;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;
import raf.si.racunovodstvo.knjizenje.model.Transakcija;
import raf.si.racunovodstvo.knjizenje.requests.TransakcijaRequest;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.ITransakcijaService;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TransakcijaControllerTest {

    @InjectMocks
    private TransakcijaController transakcijaController;

    @Mock
    private ITransakcijaService transakcijaService;

    @Mock
    private SearchUtil<Transakcija> searchUtil;

    @Mock
    private Specification<Transakcija> specificationMock;

    private static final String SEARCH_MOCK = "SEARCH_MOCK";
    private static final String TOKEN_MOCK = "TOKEN_MOCK";
    private static final Integer PAGE_MOCK = 1;
    private static final Integer SIZE_MOCK = 1;
    private static final String[] SORT_MOCK = {"SORT_MOCK"};

    @BeforeEach
    void setup() {
        Field searchUtilField = ReflectionUtils.findField(TransakcijaController.class, "searchUtil");
        searchUtilField.setAccessible(true);
        ReflectionUtils.setField(searchUtilField, transakcijaController, searchUtil);
    }

    @Test
    void findAllTest() {
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        Page<TransakcijaResponse> transakcijaResponsePage = new PageImpl<>(List.of(transakcijaResponse));
        given(transakcijaService.findAll(any(Pageable.class), anyString())).willReturn(transakcijaResponsePage);

        Page<TransakcijaResponse> result =
            transakcijaController.findAll(Strings.EMPTY, PAGE_MOCK, SIZE_MOCK, SORT_MOCK, TOKEN_MOCK).getBody();

        then(transakcijaService).should(never()).search(any(), any(), anyString());
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(transakcijaResponse, result.getContent().get(0));
    }

    @Test
    void searchTest() {
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        Page<TransakcijaResponse> transakcijaResponsePage = new PageImpl<>(List.of(transakcijaResponse));
        given(transakcijaService.search(any(), any(Pageable.class), anyString())).willReturn(transakcijaResponsePage);

        Page<TransakcijaResponse> result =
            transakcijaController.findAll(SEARCH_MOCK, PAGE_MOCK, SIZE_MOCK, SORT_MOCK, TOKEN_MOCK).getBody();

        then(transakcijaService).should(never()).findAll(any(), anyString());
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(transakcijaResponse, result.getContent().get(0));
    }

    @Test
    void createTest() {
        TransakcijaRequest transakcijaRequest = new TransakcijaRequest();
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        given(transakcijaService.save(transakcijaRequest)).willReturn(transakcijaResponse);

        TransakcijaResponse result = transakcijaController.create(transakcijaRequest).getBody();

        assertEquals(transakcijaResponse, result);
    }

    @Test
    void updateTest() {
        TransakcijaRequest transakcijaRequest = new TransakcijaRequest();
        TransakcijaResponse transakcijaResponse = new TransakcijaResponse();
        given(transakcijaService.update(transakcijaRequest)).willReturn(transakcijaResponse);

        TransakcijaResponse result = transakcijaController.update(transakcijaRequest).getBody();

        assertEquals(transakcijaResponse, result);
    }

    @Test
    void deleteTest() {
        transakcijaController.delete(1L);

        then(transakcijaService).should(times(1)).deleteById(1L);
    }
}
