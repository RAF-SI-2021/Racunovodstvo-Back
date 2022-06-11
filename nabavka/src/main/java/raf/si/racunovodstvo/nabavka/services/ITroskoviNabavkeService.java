package raf.si.racunovodstvo.nabavka.services;

import raf.si.racunovodstvo.nabavka.model.TroskoviNabavke;

public interface ITroskoviNabavkeService extends IService<TroskoviNabavke, Long>{

    TroskoviNabavke update(TroskoviNabavke troskoviNabavke);
}
