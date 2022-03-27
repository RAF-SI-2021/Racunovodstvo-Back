package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;
import rs.raf.demo.model.enums.TipDokumenta;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Dokument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dokumentId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipDokumenta tipDokumenta;
    @OneToMany(mappedBy = "dokument")
    private List<Knjizenje> knjizenje;
}
