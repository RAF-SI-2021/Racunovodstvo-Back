package raf.si.racunovodstvo.preduzece.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.racunovodstvo.preduzece.validation.groups.OnCreate;
import raf.si.racunovodstvo.preduzece.validation.groups.OnUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObracunZaposleniRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long obracunZaposleniId;
    @NotNull
    @Size(max = 1)
    private Double ucinak;
    private Double porez;
    private Double doprinos1;
    private Double doprinos2;
    @NotNull
    private Double netoPlata;
    private Double brutoPlata;
    private Double ukupanTrosakZarade;
    private String komentar;
    @NotNull
    private Long zaposleniId;
    @NotNull
    private Long obracunId;

}
