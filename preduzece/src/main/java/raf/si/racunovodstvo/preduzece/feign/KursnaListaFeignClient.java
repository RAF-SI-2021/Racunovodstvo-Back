package raf.si.racunovodstvo.preduzece.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import raf.si.racunovodstvo.preduzece.responses.KursnaListaResponse;

@FeignClient(name = "external", url = "${feign.config.kursna_lista.url}")
public interface KursnaListaFeignClient {

    @GetMapping(value = "kl_na_dan/{date}/json")
    KursnaListaResponse getKursnaListaForDate(@PathVariable String date);
}
