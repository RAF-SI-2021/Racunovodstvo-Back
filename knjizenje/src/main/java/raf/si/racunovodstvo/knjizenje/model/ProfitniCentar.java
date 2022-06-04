package raf.si.racunovodstvo.knjizenje.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ProfitniCentar extends BazniCentar {

    @ManyToOne
    @JoinColumn(name = "parentId")
    private ProfitniCentar parentProfitniCentar;
    @JsonIgnore
    @OneToMany(mappedBy = "parentProfitniCentar", cascade = CascadeType.REMOVE)
    private List<ProfitniCentar> profitniCentarList;
}
