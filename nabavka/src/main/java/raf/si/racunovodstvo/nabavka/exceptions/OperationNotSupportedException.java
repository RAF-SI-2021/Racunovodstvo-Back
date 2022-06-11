package raf.si.racunovodstvo.nabavka.exceptions;

public class OperationNotSupportedException extends RuntimeException{

    public OperationNotSupportedException(String message) {
        super(message);
    }
}
