package raf.si.racunovodstvo.knjizenje.validation.validator;

import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.requests.TransakcijaRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class TransakcijaValidator implements ConstraintValidator<ValidTransakcija, TransakcijaRequest> {

    @Override
    public boolean isValid(TransakcijaRequest transakcijaRequest, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}