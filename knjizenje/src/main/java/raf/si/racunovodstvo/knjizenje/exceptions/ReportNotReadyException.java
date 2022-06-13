package raf.si.racunovodstvo.knjizenje.exceptions;

public class ReportNotReadyException extends RuntimeException{

    public ReportNotReadyException() {
        super("Izvestaj se ne moze generisati za trenutnu godinu!");
    }
}
