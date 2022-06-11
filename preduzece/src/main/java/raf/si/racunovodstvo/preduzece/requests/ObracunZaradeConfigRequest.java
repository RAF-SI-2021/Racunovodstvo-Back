package raf.si.racunovodstvo.preduzece.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ObracunZaradeConfigRequest {
        private int dayOfMonth;
        private Long SifraTransakcijeId;
    }

