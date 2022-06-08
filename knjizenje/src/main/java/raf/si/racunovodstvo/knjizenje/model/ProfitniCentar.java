package raf.si.racunovodstvo.knjizenje.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "profitni_centar")
@Getter
@Setter
public class ProfitniCentar extends BazniCentar {

    @ManyToOne
    @JoinColumn(name = "parentId")
    private ProfitniCentar parentProfitniCentar;
    @JsonIgnore
    @OneToMany(mappedBy = "parentProfitniCentar")
    private List<ProfitniCentar> profitniCentarList;
}
