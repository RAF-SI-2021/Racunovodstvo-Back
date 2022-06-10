package raf.si.racunovodstvo.nabavka.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.AssertTrue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LokacijaRequest {

    private Long lokacijaId;
    private String naziv;
    private String adresa;

    @AssertTrue(message = "Polja ne mogu biti prazna")
    public boolean isValid() {
        return lokacijaId != null || (StringUtils.isNotBlank(naziv) && StringUtils.isNotBlank(adresa));
    }
}
