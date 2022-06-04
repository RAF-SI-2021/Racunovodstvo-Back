package raf.si.racunovodstvo.knjizenje.converter;

public interface IConverter<S, T> {

    T convert(S source);
}