package raf.si.racunovodstvo.preduzece.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.preduzece.model.Preduzece;
import raf.si.racunovodstvo.preduzece.repositories.PreduzeceRepository;
import raf.si.racunovodstvo.preduzece.responses.PreduzeceResponse;
import raf.si.racunovodstvo.preduzece.specifications.RacunSpecification;
import raf.si.racunovodstvo.preduzece.specifications.SearchCriteria;
import raf.si.racunovodstvo.preduzece.utils.SearchUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreduzeceServiceTest {

    @InjectMocks
    private PreduzeceService preduzeceService;
    @Mock
    private PreduzeceRepository preduzeceRepository;
    @Mock
    private SearchUtil<Preduzece> searchUtil;
    @Mock
    private ModelMapper modelMapper;


    private static final Long MOCK_ID = 1L;

    private static final String MOCK_SEARCH_KEY = "MOCK_KEY";
    private static final String MOCK_SEARCH_VALUE = "MOCK_VALUE";
    private static final String MOCK_SEARCH_OPERATION = "MOCK_OPERATION";

    @Test
    void save() {
        Preduzece preduzece = new Preduzece();
        given(preduzeceRepository.save(preduzece)).willReturn(preduzece);

        assertEquals(preduzece, preduzeceService.save(preduzece));
    }

    @Test
    void findById() {
        Preduzece preduzece = new Preduzece();
        given(preduzeceRepository.findByPreduzeceId(MOCK_ID)).willReturn(Optional.of(preduzece));

        assertEquals(preduzece, preduzeceService.findById(MOCK_ID).get());
    }

    @Test
    void findAll() {
        List<Preduzece> preduzeceList = new ArrayList<>();
        Specification<Preduzece> specification =
                new RacunSpecification<>(new SearchCriteria(MOCK_SEARCH_KEY, MOCK_SEARCH_VALUE, MOCK_SEARCH_OPERATION));

        lenient().when(searchUtil.getSpec("isActive:true")).thenReturn(specification);
        lenient().when(preduzeceRepository.findAll(specification)).thenReturn(preduzeceList);

        assertEquals(preduzeceList, preduzeceService.findAll());

    }

    @Test
    void deleteById() {
        Preduzece preduzece = new Preduzece();
        given(preduzeceRepository.findByPreduzeceId(MOCK_ID)).willReturn(Optional.of(preduzece));
        given(preduzeceRepository.save(preduzece)).willReturn(preduzece);
        preduzeceService.deleteById(MOCK_ID);
    }

    @Test
    void savePreduzece(){
        Preduzece preduzece = new Preduzece();
        PreduzeceResponse response = new PreduzeceResponse();

        given(preduzeceRepository.save(preduzece)).willReturn(preduzece);
        given(modelMapper.map(preduzece, PreduzeceResponse.class)).willReturn(response);

        assertEquals(response, preduzeceService.savePreduzece(preduzece));
    }

    @Test
    void findPreduzeceById(){
        Preduzece preduzece = new Preduzece();
        PreduzeceResponse response = new PreduzeceResponse();

        given(preduzeceRepository.findByPreduzeceId(MOCK_ID)).willReturn(Optional.of(preduzece));
        given(modelMapper.map(preduzece, PreduzeceResponse.class)).willReturn(response);

        assertEquals(response, preduzeceService.findPreduzeceById(MOCK_ID).get());
    }

    @Test
    void findAllPreduzece(){
        Preduzece preduzece = new Preduzece();
        PreduzeceResponse response = new PreduzeceResponse();
        List<PreduzeceResponse> responseList = new ArrayList<>();

        List<Preduzece> preduzeceList = new ArrayList<>();
        Specification<Preduzece> specification =
                new RacunSpecification<>(new SearchCriteria(MOCK_SEARCH_KEY, MOCK_SEARCH_VALUE, MOCK_SEARCH_OPERATION));

        lenient().when(searchUtil.getSpec("isActive:true")).thenReturn(specification);
        lenient().when(preduzeceRepository.findAll(specification)).thenReturn(preduzeceList);

        assertEquals(responseList, preduzeceService.findAllPreduzece());
    }
}