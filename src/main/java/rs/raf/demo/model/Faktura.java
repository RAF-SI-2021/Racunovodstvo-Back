package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Faktura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fakturaId;
    @Column
    private String kupac;
    @Column
    private String datum;
    @Column
    private String mesto;
}
