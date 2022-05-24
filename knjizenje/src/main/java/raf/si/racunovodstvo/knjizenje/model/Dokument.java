package raf.si.racunovodstvo.knjizenje.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Dokument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dokumentId;
    @Column(nullable = false)
    private String brojDokumenta;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipDokumenta tipDokumenta;
    @JsonIgnore
    @OneToMany(mappedBy = "dokument")
    private List<Knjizenje> knjizenje;
}