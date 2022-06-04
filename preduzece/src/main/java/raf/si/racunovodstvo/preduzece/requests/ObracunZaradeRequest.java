package raf.si.racunovodstvo.preduzece.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.racunovodstvo.preduzece.validation.groups.OnCreate;
import raf.si.racunovodstvo.preduzece.validation.groups.OnUpdate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObracunZaradeRequest {
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long obracunZaradeId;
    @NotNull @NotEmpty
    private String naziv;
}
