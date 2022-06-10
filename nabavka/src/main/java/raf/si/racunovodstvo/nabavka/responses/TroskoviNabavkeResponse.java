package raf.si.racunovodstvo.nabavka.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TroskoviNabavkeResponse {

    private Long troskoviNabavkeId;
    private String naziv;
    private Double cena;
}
