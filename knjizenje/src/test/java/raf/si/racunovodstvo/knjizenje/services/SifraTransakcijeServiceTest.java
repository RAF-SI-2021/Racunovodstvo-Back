package raf.si.racunovodstvo.knjizenje.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.SifraTransakcijeConverter;
import raf.si.racunovodstvo.knjizenje.converters.impl.SifraTransakcijeReverseConverter;
import raf.si.racunovodstvo.knjizenje.model.Povracaj;
import raf.si.racunovodstvo.knjizenje.model.SifraTransakcije;
import raf.si.racunovodstvo.knjizenje.repositories.SifraTransakcijeRepository;
import raf.si.racunovodstvo.knjizenje.requests.SifraTransakcijeRequest;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SifraTransakcijeServiceTest {

    @Mock
    private SifraTransakcijeRepository sifraTransakcijeRepository;

    @Mock
    private SifraTransakcijeReverseConverter sifraTransakcijeReverseConverter;

    @Mock
    private SifraTransakcijeConverter sifraTransakcijeConverter;

    @InjectMocks
    private SifraTransakcijeService sifraTransakcijeService;

    @Test
    void saveTest() {
        SifraTransakcije sifraTransakcije = new SifraTransakcije();
        SifraTransakcije saved = new SifraTransakcije();
        given(sifraTransakcijeRepository.save(sifraTransakcije)).willReturn(saved);

        assertEquals(saved, sifraTransakcijeService.save(sifraTransakcije));
    }

    @Test
    void findByIdTest() {
        SifraTransakcije sifraTransakcije = new SifraTransakcije();
        given(sifraTransakcijeRepository.findById(1L)).willReturn(Optional.of(sifraTransakcije));

        Optional<SifraTransakcije> result = sifraTransakcijeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(sifraTransakcije, result.get());
    }

    @Test
    void findAllTest() {
        List<SifraTransakcije> sifraTransakcijeList = new ArrayList<>();
        given(sifraTransakcijeRepository.findAll()).willReturn(sifraTransakcijeList);

        assertEquals(sifraTransakcijeList, sifraTransakcijeService.findAll());
    }

    @Test
    void deleteByIdTest() {
        when(sifraTransakcijeRepository.findById(1L)).thenReturn(Optional.of(new SifraTransakcije()));

        sifraTransakcijeService.deleteById(1L);

        then(sifraTransakcijeRepository).should(times(1)).deleteById(1L);
    }

    @Test
    void findAll() {
        Page<SifraTransakcijeResponse> page = Mockito.mock(Page.class);
        Page<SifraTransakcije> page2 = Mockito.mock(Page.class);
        page2.getContent().add(Mockito.mock(SifraTransakcije.class));
        given(sifraTransakcijeRepository.findAll(Pageable.unpaged())).willReturn(page2);
        given(page2.map(any(Function.class))).willReturn(page);
        assertEquals(page, sifraTransakcijeService.findAll(Pageable.unpaged(), "TOKEN"));
    }

    @Test
    void findAll2() {
        SifraTransakcijeResponse response = new SifraTransakcijeResponse();
        Page<SifraTransakcije> page = new PageImpl<>(List.of(Mockito.mock(SifraTransakcije.class)));
        given(sifraTransakcijeRepository.findAll(Pageable.unpaged())).willReturn(page);
        given(sifraTransakcijeReverseConverter.convert(any(SifraTransakcije.class))).willReturn(response);
        assertEquals(response, sifraTransakcijeService.findAll(Pageable.unpaged(), "TOKEN").getContent().get(0));
    }

    @Test
    void search() {
        SifraTransakcijeResponse response = new SifraTransakcijeResponse();
        Page<SifraTransakcije> page = new PageImpl<>(List.of(Mockito.mock(SifraTransakcije.class)));
        given(sifraTransakcijeRepository.findAll(any(Specification.class), eq(Pageable.unpaged()))).willReturn(page);
        given(sifraTransakcijeReverseConverter.convert(any(SifraTransakcije.class))).willReturn(response);
        assertEquals(response, sifraTransakcijeService.search(Mockito.mock(Specification.class), Pageable.unpaged(), "TOKEN").getContent().get(0));
    }

    @Test
    void testSave() {
        SifraTransakcijeResponse sifraTransakcijeResponse = new SifraTransakcijeResponse();
        SifraTransakcijeRequest sifraTransakcijeRequest = new SifraTransakcijeRequest();
        SifraTransakcije sifraTransakcije = new SifraTransakcije();
        given(sifraTransakcijeConverter.convert(sifraTransakcijeRequest)).willReturn(sifraTransakcije);
        given(sifraTransakcijeRepository.save(sifraTransakcije)).willReturn(sifraTransakcije);
        given(sifraTransakcijeReverseConverter.convert(sifraTransakcije)).willReturn(sifraTransakcijeResponse);

        assertEquals(sifraTransakcijeResponse, sifraTransakcijeService.save(sifraTransakcijeRequest));
    }

    @Test
    void testUpdate() {
        SifraTransakcijeResponse sifraTransakcijeResponse = new SifraTransakcijeResponse();
        SifraTransakcijeRequest sifraTransakcijeRequest = new SifraTransakcijeRequest();
        SifraTransakcije sifraTransakcije = new SifraTransakcije();
        sifraTransakcijeRequest.setSifraTransakcijeId(1L);
        given(sifraTransakcijeRepository.findById(1L)).willReturn(Optional.of(sifraTransakcije));
        given(sifraTransakcijeConverter.convert(sifraTransakcijeRequest)).willReturn(sifraTransakcije);
        given(sifraTransakcijeRepository.save(sifraTransakcije)).willReturn(sifraTransakcije);
        given(sifraTransakcijeReverseConverter.convert(sifraTransakcije)).willReturn(sifraTransakcijeResponse);

        assertEquals(sifraTransakcijeResponse, sifraTransakcijeService.update(sifraTransakcijeRequest));
    }

    @Test
    void testUpdateFail() {
        SifraTransakcijeRequest s = new SifraTransakcijeRequest();
        s.setSifraTransakcijeId(1L);
        given(sifraTransakcijeRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sifraTransakcijeService.update(s));
    }
}
