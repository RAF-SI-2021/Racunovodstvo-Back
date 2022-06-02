package raf.si.racunovodstvo.knjizenje.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.*;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "povracajId")
public class Povracaj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long povracajId;
    @Column(nullable = false)
    private String brojPovracaja;
    @Column(nullable = false)
    private Double prodajnaVrednost;
    @Column
    private Date datumPovracaja;
}
