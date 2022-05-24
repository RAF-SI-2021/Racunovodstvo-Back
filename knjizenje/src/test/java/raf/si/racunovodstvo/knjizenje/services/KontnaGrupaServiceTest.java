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
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;
import raf.si.racunovodstvo.knjizenje.repositories.KontnaGrupaRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class KontnaGrupaServiceTest {

    @InjectMocks
    private KontnaGrupaService kontnaGrupaService;

    @Mock
    private KontnaGrupaRepository kontnaGrupaRepository;

    private static final long MOCK_ID = 1234L;

    @Test
    void testFindKontnaGrupaByIdSuccess() {
        KontnaGrupa kontnaGrupa = new KontnaGrupa();
        given(kontnaGrupaRepository.findById(MOCK_ID)).willReturn(Optional.of(kontnaGrupa));

        KontnaGrupa result = kontnaGrupaService.findKontnaGrupaById(MOCK_ID);

        assertEquals(kontnaGrupa, result);
    }

    @Test
    void testFindKontnaGrupaByIdException() {
        given(kontnaGrupaRepository.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> kontnaGrupaService.findKontnaGrupaById(MOCK_ID));
    }

    @Test
    void testSave() {
        KontnaGrupa kontnaGrupa = new KontnaGrupa();
        given(kontnaGrupaRepository.save(kontnaGrupa)).willReturn(kontnaGrupa);

        KontnaGrupa result = kontnaGrupaService.save(kontnaGrupa);

        assertEquals(kontnaGrupa, result);
    }

    @Test
    void testFindAll() {
        List<KontnaGrupa> kontnaGrupaList = new ArrayList<>();
        given(kontnaGrupaRepository.findAll()).willReturn(kontnaGrupaList);

        List<KontnaGrupa> result = kontnaGrupaService.findAll();

        assertEquals(kontnaGrupaList, result);
    }

    @Test
    void testFindAllSort() {
        List<KontnaGrupa> kontnaGrupaList = new ArrayList<>();
        Pageable sort = Mockito.mock(Pageable.class);
        given(kontnaGrupaRepository.findAll(sort)).willReturn(new PageImpl<>(kontnaGrupaList));

        Page<KontnaGrupa> result = kontnaGrupaService.findAll(sort);

        assertEquals(kontnaGrupaList, result.getContent());
    }

    @Test
    void testDeleteById() {
        kontnaGrupaService.deleteById(MOCK_ID);

        then(kontnaGrupaRepository).should(times(1)).deleteById(MOCK_ID);
    }
}