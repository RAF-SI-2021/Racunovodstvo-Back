package raf.si.racunovodstvo.nabavka.validation.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ArtikalValidatorTest {

    @InjectMocks
    private ArtikalValidator artikalValidator;

    @Test
    void isValidTest() {
        ConstraintValidatorContext ctx = Mockito.mock(ConstraintValidatorContext.class);
        ArtikalRequest artikalRequest = new ArtikalRequest();
        artikalRequest.setAktivanZaProdaju(true);
        artikalRequest.setMarzaProcenat(11.2);
        artikalRequest.setPorezProcenat(11.1);
        artikalRequest.setRabatProcenat(13.5);
        artikalRequest.setNabavnaCena(159.42);
        artikalRequest.setProdajnaCena(170.3);
        assertTrue(artikalValidator.isValid(artikalRequest, ctx));
    }

    @Test
    void isValidNotAktivanTest() {
        ConstraintValidatorContext ctx = Mockito.mock(ConstraintValidatorContext.class);
        ArtikalRequest artikalRequest = new ArtikalRequest();
        artikalRequest.setAktivanZaProdaju(false);
        assertTrue(artikalValidator.isValid(artikalRequest, ctx));
    }

    @Test
    void isValidFailTest() {
        ConstraintValidatorContext ctx = Mockito.mock(ConstraintValidatorContext.class);
        ArtikalRequest artikalRequest = new ArtikalRequest();
        artikalRequest.setAktivanZaProdaju(true);
        artikalRequest.setMarzaProcenat(11.2);
        artikalRequest.setPorezProcenat(11.1);
        artikalRequest.setRabatProcenat(13.5);
        artikalRequest.setNabavnaCena(159.42);
        artikalRequest.setProdajnaCena(170.2);
        assertFalse(artikalValidator.isValid(artikalRequest, ctx));
    }
}
