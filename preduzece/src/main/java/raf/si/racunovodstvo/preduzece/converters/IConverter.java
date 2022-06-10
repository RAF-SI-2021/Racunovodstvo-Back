package raf.si.racunovodstvo.preduzece.converters;

public interface IConverter<S,T>{
    T convert(S source);
}
