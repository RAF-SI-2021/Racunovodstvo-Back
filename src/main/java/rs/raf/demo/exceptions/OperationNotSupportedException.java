package rs.raf.demo.exceptions;

public class OperationNotSupportedException extends RuntimeException{

    public OperationNotSupportedException(String message) {
        super(message);
    }
}
