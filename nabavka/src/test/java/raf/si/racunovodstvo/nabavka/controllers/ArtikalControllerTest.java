package raf.si.racunovodstvo.nabavka.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;
import raf.si.racunovodstvo.nabavka.responses.ArtikalResponse;
import raf.si.racunovodstvo.nabavka.services.IArtikalService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArtikalControllerTest {

    @InjectMocks
    private ArtikalController artikalController;

    @Mock
    private IArtikalService iArtikalService;

    @Test
    void findAllTest() {
        Page<ArtikalResponse> artikalResponsePage = new PageImpl<>(new ArrayList<>());
        given(iArtikalService.findAll(any())).willReturn(artikalResponsePage);

        ResponseEntity<Page<ArtikalResponse>> response = artikalController.findAll(null, 0, 1, new String[]{});

        assertEquals(artikalResponsePage, response.getBody());
    }

    @Test
    void findAllSizeOutOfBoundsTest() {
        assertThrows(IllegalArgumentException.class, () -> artikalController.findAll(null, 0, 0, new String[]{}));
    }

    @Test
    void findAllPageOutOfBoundsTest() {
        assertThrows(IllegalArgumentException.class, () -> artikalController.findAll(null, -1, 1, new String[]{}));
    }

    @Test
    void deleteTest() {
        ResponseEntity<String> response = artikalController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void createTest() {
        ArtikalRequest artikalRequest = new ArtikalRequest();
        ArtikalResponse expectedResponse = new ArtikalResponse();
        given(iArtikalService.save(artikalRequest)).willReturn(expectedResponse);

        ArtikalResponse actualResponse = artikalController.create(artikalRequest).getBody();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void updateTest() {
        ArtikalRequest artikalRequest = new ArtikalRequest();
        ArtikalResponse expectedResponse = new ArtikalResponse();
        given(iArtikalService.update(artikalRequest)).willReturn(expectedResponse);

        ArtikalResponse actualResponse = artikalController.update(artikalRequest).getBody();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void findAllForKonverzijaOrKalkulacijaTest() {
        Page<ArtikalResponse> artikalResponsePage = new PageImpl<>(new ArrayList<>());
        given(iArtikalService.findAllByIdKalkulacijaKonverzija(any(), any())).willReturn(artikalResponsePage);

        ResponseEntity<Page<ArtikalResponse>> response = artikalController.findAllForKonverzijaOrKalkulacija(0, 1, new String[]{}, 1L);

        assertEquals(artikalResponsePage, response.getBody());
    }
}
