package raf.si.racunovodstvo.nabavka.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TroskoviNabavkeRequest {

    private Long troskoviNabavkeId;
    @NotNull
    private String naziv;
    @NotNull
    private Double cena;
}
