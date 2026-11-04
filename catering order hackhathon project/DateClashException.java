package eventplanner;

public class DateClashException extends Exception {
    private static final long serialVersionUID = 1L;
    public DateClashException(String message) { super(message); }
}
