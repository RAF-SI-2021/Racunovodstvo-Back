package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Preduzece;
import rs.raf.demo.repositories.PreduzeceRepository;

import java.util.*;

@Service
public class PreduzeceService {

    private final PreduzeceRepository preduzeceRepository;

    @Autowired
    public PreduzeceService(PreduzeceRepository preduzeceRepository) {
        this.preduzeceRepository = preduzeceRepository;
    }

    public  List<Preduzece> findAll(){
        return preduzeceRepository.findAll();
    }

    private Preduzece save(Preduzece preduzece){
        return  preduzeceRepository.save(preduzece);
    }
}
