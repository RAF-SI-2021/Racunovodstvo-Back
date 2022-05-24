package raf.si.racunovodstvo.preduzece.exceptions;

public class OperationNotSupportedException extends RuntimeException{

    public OperationNotSupportedException(String message) {
        super(message);
    }
}
