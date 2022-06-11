package raf.si.racunovodstvo.nabavka.validation.validator;

import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.nabavka.requests.ArtikalRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ArtikalValidator implements ConstraintValidator<ValidArtikal, ArtikalRequest> {

    @Override
    public boolean isValid(ArtikalRequest artikalRequest, ConstraintValidatorContext constraintValidatorContext) {
        // ako se radi update za artikal, onda nije potrebna validacija, samo je potrebna pri kreiranju artikla
        if (artikalRequest.getArtikalId() != null) {
            return true;
        }
        if (!artikalRequest.isAktivanZaProdaju()) {
            return true;
        }
        Double nabavnaCena = artikalRequest.getNabavnaCena();
        Double rabat = nabavnaCena * (artikalRequest.getRabatProcenat() / 100);
        double nabavnaCenaPosleRabata = nabavnaCena - rabat;
        double marza = nabavnaCenaPosleRabata * (artikalRequest.getMarzaProcenat() / 100);
        double osnovicaZaProdaju = nabavnaCenaPosleRabata + marza;
        double porez = osnovicaZaProdaju * (artikalRequest.getPorezProcenat() / 100);
        double prodajnaCena = osnovicaZaProdaju + porez;

        BigDecimal calculatedBD = BigDecimal.valueOf(prodajnaCena);
        BigDecimal requestBD = BigDecimal.valueOf(artikalRequest.getProdajnaCena());
        calculatedBD = calculatedBD.setScale(1, RoundingMode.DOWN);
        requestBD = requestBD.setScale(1, RoundingMode.DOWN);

        return requestBD.equals(calculatedBD);
    }
}
