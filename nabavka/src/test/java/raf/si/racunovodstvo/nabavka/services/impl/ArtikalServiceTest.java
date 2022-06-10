package raf.si.racunovodstvo.nabavka.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import raf.si.racunovodstvo.nabavka.converters.impl.ArtikalConverter;
import raf.si.racunovodstvo.nabavka.converters.impl.ArtikalReverseConverter;
import raf.si.racunovodstvo.nabavka.model.Artikal;
import raf.si.racunovodstvo.nabavka.repositories.ArtikalRepository;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;
import raf.si.racunovodstvo.nabavka.responses.ArtikalResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ArtikalServiceTest {

    @InjectMocks
    private ArtikalService artikalService;

    @Mock
    private ArtikalRepository artikalRepository;

    @Mock
    private ArtikalConverter artikalConverter;

    @Mock
    private ArtikalReverseConverter artikalReverseConverter;

    private static final Long ARTIKAL_ID_MOCK = 1L;

    @Test
    void saveTest() {
        ArtikalRequest artikalRequest = new ArtikalRequest();
        ArtikalResponse artikalResponse = new ArtikalResponse();
        Artikal artikal = Mockito.mock(Artikal.class);
        given(artikalConverter.convert(artikalRequest)).willReturn(artikal);
        given(artikalRepository.save(artikal)).willReturn(artikal);
        given(artikalReverseConverter.convert(artikal)).willReturn(artikalResponse);

        assertEquals(artikalResponse, artikalService.save(artikalRequest));
    }

    @Test
    void updateTest() {
        ArtikalRequest artikalRequest = new ArtikalRequest();
        artikalRequest.setArtikalId(ARTIKAL_ID_MOCK);
        ArtikalResponse artikalResponse = new ArtikalResponse();
        Artikal artikal = Mockito.mock(Artikal.class);
        Optional<Artikal> optionalArtikal = Optional.of(artikal);
        given(artikalRepository.findById(ARTIKAL_ID_MOCK)).willReturn(optionalArtikal);
        given(artikalConverter.convert(artikalRequest)).willReturn(artikal);
        given(artikalRepository.save(artikal)).willReturn(artikal);
        given(artikalReverseConverter.convert(artikal)).willReturn(artikalResponse);

        assertEquals(artikalResponse, artikalService.update(artikalRequest));
    }

    @Test
    void updateExceptionTest() {
        ArtikalRequest artikalRequest = new ArtikalRequest();
        artikalRequest.setArtikalId(ARTIKAL_ID_MOCK);
        given(artikalRepository.findById(ARTIKAL_ID_MOCK)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> artikalService.update(artikalRequest));
    }

    @Test
    void deleteByIdTest() {
        given(artikalRepository.findById(ARTIKAL_ID_MOCK)).willReturn(Optional.of(Mockito.mock(Artikal.class)));

        artikalService.deleteById(ARTIKAL_ID_MOCK);

        then(artikalRepository).should(times(1)).deleteById(ARTIKAL_ID_MOCK);
    }

    @Test
    void deleteByIdExceptionTest() {
        given(artikalRepository.findById(ARTIKAL_ID_MOCK)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> artikalService.deleteById(ARTIKAL_ID_MOCK));
    }

    @Test
    void findAllPageableTest() {
        Artikal artikal = Mockito.mock(Artikal.class);
        ArtikalResponse artikalResponse = new ArtikalResponse();
        Page<Artikal> artikalPage = new PageImpl<>(List.of(artikal));
        Pageable pageable = Mockito.mock(Pageable.class);
        given(artikalRepository.findAll(pageable)).willReturn(artikalPage);
        given(artikalReverseConverter.convert(artikal)).willReturn(artikalResponse);

        Page<ArtikalResponse> result = artikalService.findAll(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(artikalResponse, result.getContent().get(0));
    }

    @Test
    void findAllByIdKalkulacijaKonverzija() {
        Artikal artikal = Mockito.mock(Artikal.class);
        ArtikalResponse artikalResponse = new ArtikalResponse();
        Page<Artikal> artikalPage = new PageImpl<>(List.of(artikal));
        Pageable pageable = Mockito.mock(Pageable.class);
        given(artikalRepository.findAllByBaznaKonverzijaKalkulacijaId(pageable, 1L)).willReturn(artikalPage);
        given(artikalReverseConverter.convert(artikal)).willReturn(artikalResponse);

        Page<ArtikalResponse> result = artikalService.findAllByIdKalkulacijaKonverzija(pageable, 1L);
        assertEquals(1, result.getTotalElements());
        assertEquals(artikalResponse, result.getContent().get(0));
    }

    @Test
    void findAllTest() {
        List<Artikal> artikalList = new ArrayList<>();
        given(artikalRepository.findAll()).willReturn(artikalList);

        assertEquals(artikalList, artikalService.findAll());
    }

    @Test
    void findByIdTest() {
        Artikal artikal = Mockito.mock(Artikal.class);
        given(artikalRepository.findById(ARTIKAL_ID_MOCK)).willReturn(Optional.of(artikal));

        assertEquals(artikal, artikalService.findById(ARTIKAL_ID_MOCK).get());
    }

    @Test
    void saveArtikalTest() {
        Artikal artikal = Mockito.mock(Artikal.class);
        given(artikalRepository.save(artikal)).willReturn(artikal);

        assertEquals(artikal, artikalService.save(artikal));
    }
}
