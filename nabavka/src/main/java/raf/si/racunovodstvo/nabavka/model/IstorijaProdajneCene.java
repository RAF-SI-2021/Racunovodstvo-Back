package raf.si.racunovodstvo.nabavka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IstorijaProdajneCene {

    private Date timestamp;

    private Double prodajnaCena;
}
