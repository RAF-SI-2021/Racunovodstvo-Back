package raf.si.racunovodstvo.knjizenje.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import raf.si.racunovodstvo.knjizenje.exceptions.OperationNotSupportedException;
import raf.si.racunovodstvo.knjizenje.services.KontoService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlavnaKnjigaControllerTest {

    @InjectMocks
    private GlavnaKnjigaController glavnaKnjigaController;
    @Mock
    private KontoService kontoService;

    @Test
    void getPreduzeceById() {
        String search = "potrazuje>=0";
        Integer page = 1;
        Integer size = 50;
        String[] sort = new String[1];
        sort[0] = "1";

        ResponseEntity<?> responseEntity = glavnaKnjigaController.getGlavnaKnjiga(search, page, size, sort);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getPreduzeceByIdException() {
        String search = "abc";
        Integer page = 1;
        Integer size = 50;
        String[] sort = new String[1];
        sort[0] = "1";

        assertThrows(OperationNotSupportedException.class, () -> glavnaKnjigaController.getGlavnaKnjiga(search, page, size, sort));
    }

    @Test
    void getAll() {
        ResponseEntity<?> responseEntity = glavnaKnjigaController.getAll();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}