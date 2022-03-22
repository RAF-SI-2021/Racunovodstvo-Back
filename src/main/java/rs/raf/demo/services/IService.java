package rs.raf.demo.services;


import java.util.List;
import java.util.Optional;

public interface IService<T, id> {
    <S extends T> S save(S var1);

    Optional<T> findById(id var1);

    List<T> findAll();

    void deleteById(id var1);

}
