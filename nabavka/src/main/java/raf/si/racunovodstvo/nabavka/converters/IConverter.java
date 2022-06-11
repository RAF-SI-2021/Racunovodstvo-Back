package raf.si.racunovodstvo.nabavka.converters;

public interface IConverter<S, T> {

    T convert(S source);
}
