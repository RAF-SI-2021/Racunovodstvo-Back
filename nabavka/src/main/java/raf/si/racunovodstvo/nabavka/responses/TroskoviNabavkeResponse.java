package raf.si.racunovodstvo.nabavka.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TroskoviNabavkeResponse implements Serializable {

    private Long troskoviNabavkeId;
    private String naziv;
    private Double cena;
}
