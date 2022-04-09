package raf.si.racunovodstvo.knjizenje.exceptions;

public class OperationNotSupportedException extends RuntimeException{

    public OperationNotSupportedException(String message) {
        super(message);
    }
}
