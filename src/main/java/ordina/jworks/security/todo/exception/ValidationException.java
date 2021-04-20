package ordina.jworks.security.todo.exception;

/**
 * @author Maarten Casteels
 */
public class ValidationException extends RuntimeException {

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }
}
