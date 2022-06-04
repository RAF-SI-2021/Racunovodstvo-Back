package raf.si.racunovodstvo.knjizenje.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class TroskovniCentar extends BazniCentar {

    @ManyToOne
    @JoinColumn(name = "parentId")
    private TroskovniCentar parentTroskovniCentar;
    @JsonIgnore
    @OneToMany(mappedBy = "parentTroskovniCentar", cascade = CascadeType.REMOVE)
    private List<TroskovniCentar> troskovniCentarList;
}
