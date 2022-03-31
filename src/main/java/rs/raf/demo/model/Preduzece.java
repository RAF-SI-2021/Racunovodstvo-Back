package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
public class Preduzece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preduzeceId;
    @Column(nullable = false)
    @NotBlank(message = "Naziv je obavezan")
    private String naziv;
    @Column(nullable = false)
    @NotBlank(message = "PIB je obavezan")
    @NumberFormat
    @Size(min = 9, max = 9)
    private String pib;
    @Column
    private String racun;
    @Column(nullable = false)
    @NotBlank(message = "Adresa je obavezna")
    private String adresa;
    @Column(nullable = false)
    @NotBlank(message = "Grad je obavezan")
    private String grad;
    @Column
    private String telefon;
    @Column
    @Email
    private String email;
    @Column
    private String fax;
    @Column
    private String webAdresa;
    @Column
    private String komentar;
    @JsonIgnore
    @OneToMany(mappedBy = "preduzece", fetch =  FetchType.EAGER)
    private List<Faktura> fakture;
    @JsonIgnore
    @OneToMany(mappedBy = "preduzece")
    private List<Zaposleni> zaposleni;
}
