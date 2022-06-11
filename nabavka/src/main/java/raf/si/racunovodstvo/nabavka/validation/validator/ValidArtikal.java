package raf.si.racunovodstvo.nabavka.validation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ArtikalValidator.class)
public @interface ValidArtikal {

    String message () default "Incorrect Marza% and ProdajnaCena";

    Class<?>[] groups () default {};

    Class<? extends Payload>[] payload () default {};
}
