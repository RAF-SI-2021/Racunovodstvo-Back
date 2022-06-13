package raf.si.racunovodstvo.knjizenje.converters;

public interface IConverter<S, T> {

    T convert(S source);
}