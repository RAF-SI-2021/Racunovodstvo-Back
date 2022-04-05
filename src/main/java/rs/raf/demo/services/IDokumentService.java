package rs.raf.demo.services;

import rs.raf.demo.model.Dokument;

public interface IDokumentService extends IService<Dokument, Long>{

    Double getSumaPotrazuje(Long id);

    Double getSumaDuguje(Long id);

    Double getSaldo(Long id);
}
