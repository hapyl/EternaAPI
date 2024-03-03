package test;

public class EternaTestException extends RuntimeException {

    final EternaTest test;

    public EternaTestException(EternaTest test, String message) {
        super(message);

        this.test = test;
    }
}
