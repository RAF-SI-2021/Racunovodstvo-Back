package raf.si.racunovodstvo.knjizenje.validation.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransakcijaValidatorTest {

    @InjectMocks
    private TransakcijaValidator transakcijaValidator;

    @Test
    public void testIsValid() {
        assertEquals(true, transakcijaValidator.isValid(null, null));
    }
}