package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class Preduzece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preduzeceId;
    @Column
    @NotBlank(message = "Naziv je obavezan")
    private String naziv;
    @Column
    @NotBlank(message = "PIB je obavezan")
    private String pib;
    @Column
    private String racun;
    @Column
    @NotBlank(message = "Adresa je obavezna")
    private String adresa;
    @Column
    @NotBlank(message = "Grad je obavezan")
    private String grad;
    @Column
    private String telefon;
    @Column
    private String email;
    @Column
    private String fax;
    @Column
    private String webAdresa;
    @Column
    private String komentar;
}
